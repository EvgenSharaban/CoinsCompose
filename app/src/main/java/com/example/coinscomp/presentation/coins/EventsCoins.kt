package com.example.coinscomp.presentation.coins

sealed interface EventsCoins {

    class MessageForUser(val message: String) : EventsCoins

}