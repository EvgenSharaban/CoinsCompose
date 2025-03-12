package com.example.coinscomp.domain.repositories

import com.example.coinscomp.data.local.room.entities.NoteRoomEntity
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    val notes: Flow<List<NoteRoomEntity>>

    suspend fun addNote(note: NoteRoomEntity): Result<Unit>

    suspend fun deleteNote(note: NoteRoomEntity): Result<Unit>

    suspend fun getTotalNotesCount(): Result<Int>

}