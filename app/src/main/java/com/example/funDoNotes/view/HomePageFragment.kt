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
import com.example.funDoNotes.model.Note
import com.example.funDoNotes.model.NoteAdapter
import com.example.loginandregistrationwithfragment.R
import com.example.loginandregistrationwithfragment.databinding.FragmentHomePageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class HomePageFragment : Fragment(R.layout.fragment_home_page) {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var floatingActionBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var tempArrayList: ArrayList<Note>
    private lateinit var notlistFromFirebase : ArrayList<Note>
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var note: Array<String>

    private lateinit var menuBtn: ImageButton

    private var noteId : String = ""
    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    var currentView = "GRID_VIEW"

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
        floatingActionBtn = view.findViewById(R.id.floatingActionBtn)
        recyclerView = view.findViewById(R.id.recycler_home)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        noteList = arrayListOf<Note>()
        tempArrayList = arrayListOf<Note>()
        notlistFromFirebase = arrayListOf<Note>()

        menuBtn = itemView.findViewById(R.id.menuImageBtn)
        menuBtn.visibility = View.GONE




//        menuBtn.setOnClickListener {
//
//
//            val currentUserId = firebaseAuth.currentUser?.uid
//            val popupMenus = PopupMenu(context, menuBtn)
//            popupMenus.inflate(R.menu.rv_popup_menu)
//            popupMenus.setOnMenuItemClickListener {
//
//                when (it.itemId) {
//
//
//                    R.id.popup_itemArchive -> {
//                        db.collection("user").document(firebaseAuth.currentUser?.uid!!)
//                            .collection("my_notes")
//                            .get().addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    for (document in it.result){
//                                        val userNote : Note = Note(document["title"].toString(),
//                                            document["subtitle"].toString(),
//                                            document["content"].toString(),
//                                            document["timestamp"] as Timestamp,
//                                            document["noteId"].toString(),
//                                            document["isArchive"] as Boolean
//                                        )
//
//                                        noteId = userNote.noteId.toString()
//                                    }
//                                }
//
//                            }
//
//
//                        updateArchiveStatus(noteId, true)
//                        val appCompatActivity = context as AppCompatActivity
//                        val fragment = ArchiveFragment()
//                        appCompatActivity.supportFragmentManager.beginTransaction()
//                            .replace(R.id.fragmentsContainer, fragment)
//                            .addToBackStack(null)
//                            .commit()
//                        it.isVisible.not()
//
//                        true
//                    }
//                    R.id.popup_itemDelete -> {
//
//                        val builder = AlertDialog.Builder(requireContext())
//                        builder.setTitle("Delete Note")
//                            .setMessage("Are you sure you want to Delete?")
//                            .setCancelable(false)
//                            .setPositiveButton("Yes") { dialog, id ->
//                                db.collection("user").document(currentUserId!!)
//                                    .collection("my_notes").document(noteId).delete()
//                                    .addOnCompleteListener {
////                                        val position = viewHold
//                                        if (it.isSuccessful) {
////                                            noteList.remove()
//                                            recyclerView.adapter?.notifyDataSetChanged()
//                                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT)
//                                                .show()
//                                        } else {
//                                            Toast.makeText(
//                                                context,
//                                                "Error while deleting",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//                            }
//                            .setNegativeButton("No") { dialog, id ->
//                                // Dismiss the dialog
//                                dialog.dismiss()
//                            }
//                        val alert = builder.create()
//                        alert.show()
//
//                        val helper = MyDbHelper(requireContext())
//                        helper.deleteOneRow(noteId)
//                        true
//                    }
//                    else -> true
//                }
//            }
//            popupMenus.show()
//            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
//            popup.isAccessible = true
//            val menu = popup.get(popupMenus)
//            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                .invoke(menu, true)
//
//
//
//
//        }


        retrievNotesFromFirestoreAndStoreToNoteList()

        floatingActionBtn.setOnClickListener {
            val fragment = CreateNewNoteFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()

            Toast.makeText(context, "Floating Action Btn clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

//    private fun updateArchiveStatus(noteId: String, isArchive : Boolean) {
//        val userId = firebaseAuth.currentUser?.uid!!
//        val docRef = db.collection("user").document(userId).collection("my_notes")
//            .document(noteId)
//        docRef.update("isArchive", isArchive).addOnCompleteListener {
//            Toast.makeText(context, "Note added to Archive list", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun retrievNotesFromFirestoreAndStoreToNoteList(){

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
                    var homeNoteList = notlistFromFirebase.filter { it.isArchive == false }
                    Log.d("Archive fragment", "$notlistFromFirebase")

                    noteList.addAll(homeNoteList)

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