package com.example.funDoNotes.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.funDoNotes.model.ArchiveNoteAdapter
import com.example.funDoNotes.model.Note
import com.example.funDoNotes.model.NoteAdapter
import com.example.loginandregistrationwithfragment.R
import com.example.loginandregistrationwithfragment.databinding.FragmentHomePageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*
import kotlin.collections.ArrayList


class HomePageFragment : Fragment(R.layout.fragment_home_page) {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var floatingActionBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var tempArrayList: ArrayList<Note>
    private lateinit var notlistFromFirebase: ArrayList<Note>
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var note: Array<String>

    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    var currentView = "GRID_VIEW"
    private lateinit var lastVisible : DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomePageBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity?)?.setDrawerUnlocked()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        val itemView = inflater.inflate(R.layout.notes_list_view, container, false)
        (activity as MainActivity).supportActionBar?.setTitle(R.string.home_title)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
//        val currentUserId = firebaseAuth.currentUser?.uid!!
        floatingActionBtn = view.findViewById(R.id.floatingActionBtn)
        recyclerView = view.findViewById(R.id.recycler_home)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        noteList = arrayListOf<Note>()
        tempArrayList = arrayListOf<Note>()
        notlistFromFirebase = arrayListOf<Note>()
//        getNotes()

        retrievNotesFromFirestoreAndStoreToNoteList()



        floatingActionBtn.setOnClickListener {
            val fragment = CreateNewNoteFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()

            Toast.makeText(context, "Floating Action Btn clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun getNotes() {
        val notes = getNoteService.noteIntance.getNotes()
        notes.enqueue(object : Callback<List<Note?>?>{
            override fun onResponse(call: Call<List<Note?>?>, response: Response<List<Note?>?>) {
                val noteListAPI : List<Note?>? = response.body()
//                var homeNoteList = noteListAPI?.filter { it.isArchive == false }
                Log.d("Home fragment**", noteListAPI.toString())
//                if (noteListAPI != null) {
//                    noteList.add(noteListAPI)
//                }

//                if (homeNoteList != null) {
//                    noteList.addAll(homeNoteList)
//                }

                tempArrayList.addAll(noteList)
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.adapter = NoteAdapter(requireContext(), tempArrayList)
            }

            override fun onFailure(call: Call<List<Note?>?>, t: Throwable) {
                Toast.makeText(requireContext(), t.message,
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun retrievNotesFromFirestoreAndStoreToNoteList() {

        db.collection("user").document(firebaseAuth.currentUser?.uid!!)
            .collection("my_notes")
            .get().addOnCompleteListener {
                var notlistFromFirebase : ArrayList<Note> = arrayListOf<Note>()
                if (it.isSuccessful){
                    for (document in it.result){
                        val userNote : Note = Note(document["title"].toString(),
                            document["subtitle"].toString(),
                            document["content"].toString(),
                            document["timestamp"] as Timestamp,
                            document["noteId"].toString(),
                            document["isArchive"] as Boolean
                        )
                        notlistFromFirebase.add(userNote)

                    }
                    var filterNoteList = notlistFromFirebase.filter { it.isArchive == false }
                    Log.d("Archive fragment", "$notlistFromFirebase")

                    noteList.addAll(filterNoteList)
                    tempArrayList.addAll(noteList)
                    tempArrayList.sortByDescending {
                        it.timestamp
                    }
                    recyclerView.adapter = NoteAdapter(requireContext(), tempArrayList)
                }

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }

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

                    noteList.forEach {
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
                    tempArrayList.addAll(noteList)
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
        recyclerView.adapter = NoteAdapter(requireContext(), noteList)

    }

    private fun gridView() {
        currentView = GRID_VIEW
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = NoteAdapter(requireContext(), noteList)
    }

}