package com.example.coinscomp.presentation.coins

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.coinscomp.R
import com.example.coinscomp.core.other.TAG
import com.example.coinscomp.domain.repositories.CoinsRepository
import com.example.coinscomp.presentation.base.BaseViewModel
import com.example.coinscomp.presentation.coins.models.CoinUiModelMapper.mapToUiModel
import com.example.coinscomp.presentation.coins.models.ModelCoinsCustomView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val _coinList = MutableStateFlow<List<ModelCoinsCustomView>>(emptyList())
    val coinsList = _coinList.asStateFlow()

    private val _event = Channel<EventsCoins>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()


    init {
        getCoins()
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            coinsRepository.coins.collect { localCoins ->
                val coinsList = localCoins.map { it.mapToUiModel() }
                Log.d(TAG, "observeData: _coinForCustomViewList size = ${_coinList.value.size}, coinsList size = ${coinsList.size}")
                if (_coinList.value != coinsList) {
                    _coinList.update { coinsList }
                }
            }
        }
    }

    private fun getCoins() {
        viewModelScope.launch {
            setLoading(true)
            if (hasInternetConnection()) {
                coinsRepository.fetchCoinsFullEntity()
                    .onFailure { error ->
                        _event.send(EventsCoins.MessageForUser(error.message ?: context.getString(R.string.unknown_error)))
                    }
            } else {
                val message = context.getString(R.string.no_internet_connection)
                _event.send(EventsCoins.MessageForUser(message))
                Log.d(TAG, message)
            }
            setLoading(false)
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}