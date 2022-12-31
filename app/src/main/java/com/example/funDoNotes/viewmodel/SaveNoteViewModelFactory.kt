package com.example.funDoNotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.funDoNotes.model.NoteService

class SaveNoteViewModelFactory(val noteService: NoteService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaveNoteViewModel(noteService) as T
    }
}