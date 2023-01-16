package com.example.funDoNotes.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.funDoNotes.model.Note
import com.example.funDoNotes.model.NoteAdapter

import com.example.loginandregistrationwithfragment.R
import com.example.loginandregistrationwithfragment.databinding.FragmentArchiveBinding
import com.example.loginandregistrationwithfragment.databinding.FragmentHomePageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class ArchiveFragment : Fragment() {
    private lateinit var binding: FragmentArchiveBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var archivrNoteList: ArrayList<Note>
    private lateinit var tempArrayList: ArrayList<Note>
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var note: Array<String>
    private var archiveStatus: Boolean = false

    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    var currentView = "GRID_VIEW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentArchiveBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archive, container, false)
        (activity as MainActivity).supportActionBar?.setTitle(R.string.archive_title)

        firebaseAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.recycler_Archive)
        val currentUserId = firebaseAuth.currentUser?.uid!!
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        archivrNoteList = arrayListOf<Note>()
        tempArrayList = arrayListOf<Note>()


        db = FirebaseFirestore.getInstance()
        db.collection("user").document(currentUserId)
            .collection("my_notes")
            .get().addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val note: Note? = data.toObject(Note::class.java)
                        val noteId = note?.noteId.toString()
                        val docRef = db.collection("user").document(currentUserId)
                            .collection("my_notes").document(noteId)
                        docRef.get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                archiveStatus = it.result.getBoolean("isArchive")!!
                            }
                        }
                        if (note != null) {
                            if (archiveStatus == true){
                                archivrNoteList.add(note)
                            }

                        }

                    }
                    tempArrayList.addAll(archivrNoteList)
                    recyclerView.adapter = NoteAdapter(requireContext(), tempArrayList)

                    archivrNoteList.sortByDescending {
                        it.timestamp
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.opt_switchView -> {

                if (currentView == GRID_VIEW) {
                    listView()
                    item.icon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_grid_view_24
                    )
                    currentView = LIST_VIEW
                } else {
                    gridView()
                    item.icon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_linear_view_24
                    )
                    currentView = GRID_VIEW
                }
                true
            }
            R.id.opt_search -> {
                Toast.makeText(requireContext(), "Clicked on search", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.homepage_opt_menu, menu)

        val item = menu.findItem(R.id.opt_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {

                    archivrNoteList.forEach {
                        if (it.title?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true ||
                            it.subtitle?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true ||
                            it.content?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {

                            tempArrayList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(archivrNoteList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun listView() {
        currentView = LIST_VIEW
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NoteAdapter(requireContext(), archivrNoteList)

    }

    private fun gridView() {
        currentView = GRID_VIEW
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = NoteAdapter(requireContext(), archivrNoteList)
    }

}

