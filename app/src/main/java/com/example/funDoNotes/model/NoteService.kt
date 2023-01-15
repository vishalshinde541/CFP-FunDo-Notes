package com.example.funDoNotes.model

import android.content.Context
import android.widget.Toast
import com.example.funDoNotes.view.MainActivity
import com.example.funDoNotes.view.MyDbHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteService(private val myDbHelper: MyDbHelper, var context: Context) {

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
            .collection("my_notes").document().id

        val noteMap = hashMapOf(
            "title" to note.title,
            "subtitle" to note.subtitle,
            "content" to note.content,
            "timestamp" to note.timestamp,
            "noteId" to note.noteId,
            "isArchive" to note.isArchive
        )

        saveNoteToSQLite(note)
        database.collection("user").document(currentUserId).collection("my_notes")
            .document(note.noteId!!).set(noteMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(AuthListener(true, "Note added successfully"))
                } else {
                    listener(AuthListener(false, "Failed adding notes"))
                }
            }
    }

    private fun saveNoteToSQLite(note: Note) {
        var helper = MyDbHelper(context)
        helper.addNote(
            note.noteId.toString(),
            note.title.toString(), note.subtitle.toString(),
            note.content.toString(), note.timestamp?.toDate().toString()
        )
    }

}