package com.example.funDoNotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funDoNotes.model.Note
import com.example.funDoNotes.model.NoteAdapter
import com.example.loginandregistrationwithfragment.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomePageFragment : Fragment(R.layout.fragment_home_page) {

    private lateinit var floatingActionBtn : FloatingActionButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        floatingActionBtn = view.findViewById(R.id.floatingActionBtn)
        recyclerView = view.findViewById(R.id.recycler_home)
        recyclerView.layoutManager = LinearLayoutManager(context)

        noteList = arrayListOf()

        db = FirebaseFirestore.getInstance()
        db.collection("user").document(firebaseAuth.currentUser?.uid.toString()).collection("my_notes")
            .get().addOnSuccessListener {

                if (!it.isEmpty){
                    for (data in it.documents){
                        val note: Note? = data.toObject(Note::class.java)
                        if (note != null) {
                            noteList.add(note)
                        }
                    }
                    recyclerView.adapter = NoteAdapter(noteList)

                    noteList.sortByDescending {
                        it.timestamp
                    }
                }
        }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }

        floatingActionBtn.setOnClickListener {
            val fragment = CreateNewNoteFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()

            Toast.makeText(context, "Floating Action Btn clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }



}