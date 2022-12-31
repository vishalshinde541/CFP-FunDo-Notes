package com.example.funDoNotes.model

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
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

    fun saveNoteToFirebase(note: Note,  listener: (AuthListener)-> Unit) {
        var currentUserId = firebaseAuth.currentUser?.uid!!
         database.collection("user").document(currentUserId).collection("my_notes")
        .document().set(note)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(AuthListener(true, "Note added successfully"))
                }else{
                    listener(AuthListener(false, "Failed adding notes"))
                }
            }
    }

}