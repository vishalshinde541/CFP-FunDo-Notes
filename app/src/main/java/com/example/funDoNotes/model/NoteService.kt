package com.example.funDoNotes.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteService {

    private lateinit var database: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    fun saveNoteToFirebase(note: Note, listener: (AuthListener) -> Unit) {
        var currentUserId = firebaseAuth.currentUser?.uid!!

        note.noteId = database.collection("user").document(currentUserId)
            .collection("my_notes").document().id.toString()

        val noteMap = hashMapOf(
            "title" to note.title,
            "subtitle" to note.subtitle,
            "content" to note.content,
            "timestamp" to note.timestamp,
            "noteId" to note.noteId
        )
        database.collection("user").document(currentUserId).collection("my_notes")
            .document(note.noteId).set(noteMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(AuthListener(true, "Note added successfully"))
                } else {
                    listener(AuthListener(false, "Failed adding notes"))
                }
            }
    }

}