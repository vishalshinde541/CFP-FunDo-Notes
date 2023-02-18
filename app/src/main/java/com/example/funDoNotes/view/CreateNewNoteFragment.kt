package com.example.funDoNotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.funDoNotes.model.Note
import com.example.funDoNotes.model.NoteService
import com.example.funDoNotes.viewmodel.SaveNoteViewModel
import com.example.funDoNotes.viewmodel.SaveNoteViewModelFactory
import com.example.loginandregistrationwithfragment.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateNewNoteFragment : Fragment() {

    private lateinit var saveNoteViewModel: SaveNoteViewModel
    private lateinit var database: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var titleEditText: EditText
    private lateinit var subTitleEditText: EditText
    private lateinit var contentEditText: EditText

    private lateinit var backImageButton: ImageButton
    private lateinit var reminderImageButton: ImageButton
    private lateinit var saveNoteButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saveNoteViewModel =
            ViewModelProvider(
                this,
                SaveNoteViewModelFactory(
                    NoteService(
                        MyDbHelper(requireContext()),
                        requireContext()
                    )
                )
            ).get(
                SaveNoteViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_new_note, container, false)

        titleEditText = view.findViewById(R.id.addNote_titleText)
        subTitleEditText = view.findViewById(R.id.addNote_SubtitleText)
        contentEditText = view.findViewById(R.id.addNote_contentText)
        saveNoteButton = view.findViewById(R.id.saveNoteBtn)
        backImageButton = view.findViewById(R.id.backBtn)
        reminderImageButton = view.findViewById(R.id.reminderBtn)

        checkNEtworkAndSaveNoteTOSqliteOrFirebase()

        backImageButton.setOnClickListener {
            val fragment = HomePageFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        reminderImageButton.setOnClickListener {
            val fragmentManager = fragmentManager
            val newFragment = SetReminderFragment()
            newFragment.show(fragmentManager!!, "dialog Fragment")
            true
        }

        return view
    }

    private fun checkNEtworkAndSaveNoteTOSqliteOrFirebase(){
        var noteTitle: String = titleEditText.text.toString()
        var noteSubTitle: String = subTitleEditText.text.toString()
        var noteContent: String = contentEditText.text.toString()

        var note = Note(noteTitle, noteSubTitle, noteContent, Timestamp.now())

        val mainActivity = MainActivity()
        if (mainActivity.checkForInternet(requireContext())) {
            saveNoteToFirestore()
            Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show()

        } else {
            saveNoteToSQLite(note)
            Toast.makeText(context, "Network Disconnected", Toast.LENGTH_SHORT).show()
        }


    }
    private fun saveNoteToFirestore() {

        saveNoteButton.setOnClickListener {

            var noteTitle: String = titleEditText.text.toString()
            var noteSubTitle: String = subTitleEditText.text.toString()
            var noteContent: String = contentEditText.text.toString()

            if (noteTitle == null || noteTitle.isEmpty()) {
                titleEditText.setError("Title is required")
            }
            var note = Note(noteTitle, noteSubTitle, noteContent, Timestamp.now())

            saveNoteViewModel.saveNotes(note)
            saveNoteViewModel._saveNoteStatus.observe(viewLifecycleOwner, Observer {

                if (it.status) {
                    val fragment = HomePageFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            })

        }

    }
    private fun saveNoteToSQLite(note: Note) {
        var helper = MyDbHelper(requireContext())
        helper.addNote(
            note.noteId.toString(),
            note.title.toString(), note.subtitle.toString(),
            note.content.toString(), note.timestamp?.toDate().toString()
        )
    }


}