package com.example.funDoNotes.model

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.funDoNotes.view.MyDbHelper
import com.example.funDoNotes.view.UpdateNoteFragment
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class NoteAdapter(private val context: Context, private val noteList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var noteFilterList = ArrayList<Note>()
    private var database: FirebaseFirestore
    private var firebaseAuth: FirebaseAuth

    init {
        noteFilterList = noteList
        database = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.notesContainer_tvTitle)
        val subTitle: TextView = itemView.findViewById(R.id.notesContainer_tvSubtitle)
        val noteContent: TextView = itemView.findViewById(R.id.notesContainer_tvNoteContent)
        val date: TextView = itemView.findViewById(R.id.notesContainer_tvDate)
        val note_layout: CardView = itemView.findViewById(R.id.notes_container)
        val menuBtn: ImageButton = itemView.findViewById(R.id.menuImageBtn)
        val lLayoutRow: LinearLayout = itemView.findViewById(R.id.ll_ViewRow)
        var noteId: String = ""

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.notes_list_view, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.title.text = noteList[position].title
        holder.subTitle.text = noteList[position].subtitle
        holder.noteContent.text = noteList[position].content
        holder.date.text = noteList[position].timestamp?.toDate().toString()
        holder.note_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColour()))
        holder.noteId = noteList[position].noteId as String

        holder.menuBtn.setOnClickListener {
            popupMenu(it, holder.noteId, position)
        }
        holder.lLayoutRow.setOnClickListener {
            val appCompatActivity = it.context as AppCompatActivity
            val noteId = noteList[position].noteId
            val fragment = UpdateNoteFragment()
            val bundle = Bundle()
            bundle.putString("NoteId", noteId)
            fragment.arguments = bundle
            appCompatActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentsContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun popupMenu(itemView: View, noteId: String, position: Int) {
        val currentUserId = firebaseAuth.currentUser?.uid
        val popupMenus = PopupMenu(context, itemView)
        popupMenus.inflate(R.menu.rv_popup_menu)
        popupMenus.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.popup_itemArchive -> {
                    val isArchive : Boolean = true
                    val currentNoteId = noteList[position].noteId as String
                    updateArchiveStatus(currentNoteId, isArchive)
                    Toast.makeText(context, "Archive Notes Clicked", Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                    true
                }
                R.id.popup_itemDelete -> {

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Note")
                        .setMessage("Are you sure you want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { dialog, id ->
                            database.collection("user").document(currentUserId!!)
                                .collection("my_notes").document(noteId).delete()
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        noteList.remove(noteList[position])
                                        notifyDataSetChanged()
                                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error while deleting",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                        .setNegativeButton("No") { dialog, id ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                    val helper = MyDbHelper(context)
                    helper.deleteOneRow(noteId)
                    true
                }
                else -> true
            }
        }
        popupMenus.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenus)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    private fun updateArchiveStatus(noteId: String, isArchive : Boolean) {
        val userId = firebaseAuth.currentUser?.uid!!
        val docRef = database.collection("user").document(userId).collection("my_notes")
            .document(noteId)
        docRef.update("isArchive", isArchive).addOnCompleteListener {
            Toast.makeText(context, "Note added to Archive list", Toast.LENGTH_SHORT).show()
        }
    }

    private fun randomColour(): Int {
        val colourList = ArrayList<Int>()
        colourList.add(R.color.NoteColour1)
        colourList.add(R.color.NoteColour2)
        colourList.add(R.color.NoteColour3)
        colourList.add(R.color.NoteColour4)
        colourList.add(R.color.NoteColour5)
        colourList.add(R.color.NoteColour6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(colourList.size)
        return colourList[randomIndex]
    }

    override fun getItemCount(): Int {
        return noteFilterList.size
    }

}