package com.example.coinscomp.presentation.summary

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinscomp.R
import com.example.coinscomp.core.other.FAILURE_VALUE
import com.example.coinscomp.core.other.TAG
import com.example.coinscomp.domain.repositories.CoinsRepository
import com.example.coinscomp.domain.repositories.NotesRepository
import com.example.coinscomp.domain.repositories.StatisticRepository
import com.example.coinscomp.domain.usecases.DayWithMostNotes
import com.example.coinscomp.presentation.summary.models.SummaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    private val notesRepository: NotesRepository,
    private val statisticRepository: StatisticRepository,
    private val dayWithMostNotesUseCase: DayWithMostNotes,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _summaryUiState = MutableStateFlow<SummaryScreenState>(SummaryScreenState.DEFAULT)
    val summaryUiState = _summaryUiState.asStateFlow()

    private val _event = Channel<String>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private var showingSnackBarBlocked = false

    init {
        initFetchingData()
    }


    fun initFetchingData() {
        viewModelScope.launch {
            _summaryUiState.update { it.copy(isLoading = true) }
            fetchAllData()
            _summaryUiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun fetchAllData() = withContext(Dispatchers.IO) {
        val totalItems = async { statisticRepository.getTotalItemsCount().doWithException() }
        val hiddenCoins = async { coinsRepository.getHiddenCoinsCount().doWithException() }
        val totalNotes = async { notesRepository.getTotalNotesCount().doWithException() }
        val dayWithMostNotes = async { dayWithMostNotesUseCase.getDayWithMostNotesCount().doWithException() }
        val amountOfDaysAppUsing = async { statisticRepository.getAmountOfDaysAppUsing().doWithException() }

        val totalItemsResult = totalItems.await()
        val hiddenCoinsResult = hiddenCoins.await()
        val totalNotesResult = totalNotes.await()
        val dayWithMostNotesResult = dayWithMostNotes.await()
        val amountOfDaysAppUsingResult = amountOfDaysAppUsing.await()

        _summaryUiState.update {
            it.copy(
                summaryState = SummaryState.Loaded(
                    SummaryUi(
                        totalItemsCount = totalItemsResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                        hiddenCoinsCount = hiddenCoinsResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                        totalNotesCount = totalNotesResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                        dayWithMostNotes = dayWithMostNotesResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                        amountOfDaysAppUsing = amountOfDaysAppUsingResult.getOrNull()?.toString() ?: FAILURE_VALUE
                    )
                )
            )
        }

        showingSnackBarBlocked = false
    }

    // for showing only first getting exception message
    private suspend fun <T> Result<T>.doWithException(): Result<T> {
        val exception = this.exceptionOrNull()
        Log.d(TAG, "fetchAllData: result = $this, exception = $exception, showingSnackBarBlocked = $showingSnackBarBlocked")
        if (exception != null && !showingSnackBarBlocked) {
            showingSnackBarBlocked = true
            _event.send(exception.message ?: context.getString(R.string.unknown_error))
        }
        return this
    }
}