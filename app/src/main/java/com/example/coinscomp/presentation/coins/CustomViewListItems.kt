package com.example.coinscomp.presentation.coins

import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.coins.models.notes.ModelNotesCustomView

sealed class CustomViewListItems {

    data class CoinItem(val coin: ModelCoinsCustomView) : CustomViewListItems()
    data class NoteItem(val note: ModelNotesCustomView) : CustomViewListItems()

}