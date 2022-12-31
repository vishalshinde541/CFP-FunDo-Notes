package com.example.funDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funDoNotes.model.AuthListener
import com.example.funDoNotes.model.Note
import com.example.funDoNotes.model.NoteService


class SaveNoteViewModel(var noteService: NoteService): ViewModel() {

    private var saveNoteStatus = MutableLiveData<AuthListener>()
    val _saveNoteStatus: LiveData<AuthListener> = saveNoteStatus

    fun saveNotes(note: Note) {
        noteService.saveNoteToFirebase(note, {
            saveNoteStatus.value = it
        })
    }
}