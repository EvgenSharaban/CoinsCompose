package com.example.coinscomp.presentation.coins

import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.coins.models.notes.ModelNotesCustomView

sealed interface HomeIntent {

    class CoinClick(val model: ModelCoinsCustomView) : HomeIntent
    class CoinLongClick(val model: ModelCoinsCustomView) : HomeIntent
    class NoteLongClick(val model: ModelNotesCustomView) : HomeIntent
    class AddNote(val note: String) : HomeIntent

}