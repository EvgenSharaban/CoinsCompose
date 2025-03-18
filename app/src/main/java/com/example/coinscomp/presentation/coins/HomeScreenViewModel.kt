package com.example.coinscomp.presentation.coins

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.coinscomp.R
import com.example.coinscomp.core.other.TAG
import com.example.coinscomp.data.local.room.entities.NoteRoomEntity
import com.example.coinscomp.domain.repositories.CoinsRepository
import com.example.coinscomp.domain.repositories.NotesRepository
import com.example.coinscomp.presentation.base.BaseViewModel
import com.example.coinscomp.presentation.coins.models.coins.CoinUiModelMapper.mapToUiModel
import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.coins.models.notes.ModelNotesCustomView
import com.example.coinscomp.presentation.coins.models.notes.NoteUiModelMapper.mapToNoteUiModel
import com.example.coinscomp.presentation.coins.models.notes.NoteUiModelMapper.mapToRoomModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    private val notesRepository: NotesRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val coinsList = MutableStateFlow<List<ModelCoinsCustomView>>(emptyList())
    private val notesList = MutableStateFlow<List<ModelNotesCustomView>>(emptyList())
    private val expandedCoinItemsIds = MutableStateFlow<Set<String>>(emptySet())

    val itemsList: StateFlow<List<CustomViewListItems>> = combine(coinsList, notesList, expandedCoinItemsIds) { coins, notes, expandedCoinItemsIds ->
        val notesItemsList = notes.map {
            CustomViewListItems.NoteItem(it)
        }
        val coinsItemsList = coins.map {
            val coin = ModelCoinsCustomView(
                id = it.id,
                rank = it.rank,
                name = it.name,
                description = it.description,
                creationDate = it.creationDate,
                logo = it.logo,
                shortName = it.shortName,
                type = it.type,
                isActive = it.isActive,
                price = it.price,
                isExpanded = it.id in expandedCoinItemsIds,
                isHided = it.isHided
            )
            CustomViewListItems.CoinItem(coin)
        }
        notesItemsList.plus(coinsItemsList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _event = Channel<EventsCoins>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()


    init {
        getCoins()
        observeData()
    }

    fun handleIntent(homeIntent: HomeIntent) {
        when (homeIntent) {
            is HomeIntent.AddNote -> {
                addNote(homeIntent.note)
            }

            is HomeIntent.CoinClick -> {
                onItemCoinClicked(homeIntent.model)
            }

            is HomeIntent.CoinLongClick -> {
                onItemCoinLongClicked(homeIntent.model)
            }

            is HomeIntent.NoteLongClick -> {
                onItemNoteLongClicked(homeIntent.model.mapToRoomModel())
            }
        }
    }

    private fun onItemCoinClicked(item: ModelCoinsCustomView) {
        Log.d(TAG, "onItemCoinClicked: clickedId = ${item.id}, expandedCoinItemsIds = ${expandedCoinItemsIds.value}")
        expandedCoinItemsIds.update {
            if (expandedCoinItemsIds.value.contains(item.id)) {
                it.minus(item.id)
            } else {
                it.plus(item.id)
            }
        }
    }

    private fun onItemCoinLongClicked(item: ModelCoinsCustomView) {
        setHiddenCoinsToStorage(item.id)
    }

    private fun addNote(noteText: String) {
        viewModelScope.launch {
            setLoading(true)
            val note = NoteRoomEntity(
                id = UUID.randomUUID().toString(),
                note = noteText
            )
            notesRepository.addNote(note)
            setAddedPositionToChannel()
            setLoading(false)
        }
    }

    private fun onItemNoteLongClicked(note: NoteRoomEntity) {
        viewModelScope.launch {
            setLoading(true)
            notesRepository.deleteNote(note)
            setLoading(false)
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            coinsRepository.coins.collect { localCoins ->
                val coins = localCoins.map { it.mapToUiModel() }
                Log.d(TAG, "observeData: _coinForCustomViewList size = ${coinsList.value.size}, coinsList size = ${coins.size}")
                if (coinsList.value != coins) {
                    coinsList.update { coins }
                }
            }
        }

        viewModelScope.launch {
            notesRepository.notes.collect { localNotes ->
                Log.d(TAG, "observeData: notes size = ${localNotes.size}")
                notesList.update {
                    localNotes.map { note ->
                        note.mapToNoteUiModel()
                    }
                }
            }
        }
    }

    private fun setHiddenCoinsToStorage(id: String) {
        viewModelScope.launch {
            coinsRepository.hideCoin(id)
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

    private suspend fun setAddedPositionToChannel() {
        delay(50)
        val position = if (notesList.value.isNotEmpty()) {
            notesList.value.size - 1
        } else 0
        _event.send(EventsCoins.PositionToScrolling(position))
    }
}