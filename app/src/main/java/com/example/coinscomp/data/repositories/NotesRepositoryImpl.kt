package com.example.coinscomp.data.repositories

import com.example.coinscomp.core.other.FAILURE_VALUE
import com.example.coinscomp.data.local.room.NotesDao
import com.example.coinscomp.data.local.room.entities.NoteRoomEntity
import com.example.coinscomp.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao
) : NotesRepository {

    override val notes: Flow<List<NoteRoomEntity>>
        get() = notesDao.getAllNotes()

    override suspend fun addNote(note: NoteRoomEntity): Result<Unit> {
        return try {
            notesDao.addNote(note)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(note: NoteRoomEntity): Result<Unit> {
        return try {
            notesDao.deleteNote(note)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun getTotalNotesCount(): Result<Int> {
        return try {
            val list = notesDao.getAllNotes().first()
            Result.success(list.size)
        } catch (e: Throwable) {
            Result.failure(Exception(FAILURE_VALUE, e))
        }
    }
}