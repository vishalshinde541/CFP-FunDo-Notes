package com.example.funDoNotes.model

import com.google.firebase.Timestamp


data class Note(var title: String? = null, var subtitle: String? = null,
                var content: String? = null, var timestamp: Timestamp? = null, var noteId: String? = null, var isArchive : Boolean = false){
    
}
