package com.example.coinscomp.domain.usecases

interface DayWithMostNotes {

    suspend fun getDayWithMostNotesCount(): Result<String>

}