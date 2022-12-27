package com.example.funDoNotes.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginandregistrationwithfragment.R


class CreateNewNoteFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText

    private lateinit var saveImageButton: ImageButton
    private lateinit var backImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_new_note, container, false)

        titleEditText = view.findViewById(R.id.addNote_titleText)
        contentEditText = view.findViewById(R.id.addNote_contentText)
        saveImageButton = view.findViewById(R.id.saveNoteBtn)
        backImageButton = view.findViewById(R.id.backBtn)

        saveImageButton.setOnClickListener {

            saveNote()
            Toast.makeText(requireContext(), "Clicked on save notes", Toast.LENGTH_SHORT).show()
        }

        backImageButton.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked on back button", Toast.LENGTH_SHORT).show()
            val fragment = HomePageFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        return view
    }

    private fun saveNote() {
        var noteTitle : String = titleEditText.text.toString()
        var noteContent : String = contentEditText.text.toString()
        if (noteTitle == null || noteTitle.isEmpty()){
            titleEditText.setError("Title is required")
        }
    }

}