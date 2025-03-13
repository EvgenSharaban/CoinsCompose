package com.example.coinscomp.presentation.coins.models.notes

import com.example.coinscomp.data.local.room.entities.NoteRoomEntity

object NoteUiModelMapper {

    fun NoteRoomEntity.mapToNoteUiModel(): ModelNotesCustomView {
        return ModelNotesCustomView(
            id = this.id,
            note = this.note
        )
    }

    fun ModelNotesCustomView.mapToRoomModel(): NoteRoomEntity {
        return NoteRoomEntity(
            id = this.id,
            note = this.note
        )
    }
}