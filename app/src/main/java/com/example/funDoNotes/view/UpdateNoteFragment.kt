package com.example.funDoNotes.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.funDoNotes.model.Note
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateNoteFragment : Fragment() {

    private lateinit var database: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var updateTitle: EditText
    private lateinit var updateSubTitle: EditText
    private lateinit var updateContent: EditText
    private lateinit var updateBtn: Button
    private lateinit var backBtn: ImageButton
    private lateinit var noteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_note, container, false)

        // Receiving data(NoteId) from adapter using Bundle
        val bundle = arguments
        if (bundle != null) {
             noteId = bundle.getString("NoteId").toString()

        }

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        updateTitle = view.findViewById(R.id.updateNote_titleText)
        updateSubTitle = view.findViewById(R.id.updateNote_SubtitleText)
        updateContent = view.findViewById(R.id.updateNote_contentText)
        updateBtn = view.findViewById(R.id.note_updateBtn)
        backBtn = view.findViewById(R.id.updateNote_backBtn)



        backBtn.setOnClickListener {
            val fragment = HomePageFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        readAndShowNoteDataFromFirestore()

        updateBtn.setOnClickListener {


            var noteTitle: String = updateTitle.text.toString()
            var noteSubTitle: String = updateSubTitle.text.toString()
            var noteContent: String = updateContent.text.toString()

//        var timestamp: String = timestamp.toString()
            if (noteTitle == null || noteTitle.isEmpty()) {
                updateTitle.setError("Title is required")
            }
            var note = Note(noteTitle, noteSubTitle, noteContent, Timestamp.now())

            var currentUserId = firebaseAuth.currentUser?.uid!!


            val noteMap = hashMapOf(
                "title" to note.title,
                "subtitle" to note.subtitle,
                "content" to note.content,
                "timestamp" to note.timestamp,
                "noteId" to noteId

            )

            var sqlId = noteId

            database.collection("user").document(currentUserId).collection("my_notes")
                .document(noteId).set(noteMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Note updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val fragment = HomePageFragment()
                        val transaction = fragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update note",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            var helper = MyDbHelper(requireContext())
            helper.updateData(sqlId, note.title.toString(), note.subtitle.toString(), note.content.toString(), note.timestamp.toString())
        }


        return view
    }

    private fun readAndShowNoteDataFromFirestore() {
        var currentUserId = firebaseAuth.currentUser?.uid!!


        val docRef = database.collection("user").document(currentUserId)
            .collection("my_notes").document(noteId)
        docRef.get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    updateTitle.setText(it.result.getString("title"))
                    updateSubTitle.setText(it.result.getString("subtitle"))
                    updateContent.setText(it.result.getString("content"))

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }


            }
    }


}