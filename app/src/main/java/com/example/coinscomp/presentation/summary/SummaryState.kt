package com.example.coinscomp.presentation.summary

import com.example.coinscomp.presentation.summary.models.SummaryUi

sealed interface SummaryState {
    class Default : SummaryState
    class Loaded(val value: SummaryUi) : SummaryState
}

data class SummaryScreenState(
    val summaryState: SummaryState,
    val isLoading: Boolean
) {

    companion object {

        val DEFAULT = SummaryScreenState(
            summaryState = SummaryState.Default(),
            isLoading = true
        )
    }
}