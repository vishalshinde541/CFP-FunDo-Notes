package com.example.funDoNotes.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginandregistrationwithfragment.R

class NoteAdapter(private val noteList: ArrayList<Note>): RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.notesContainer_tvTitle)
        val subTitle: TextView = itemView.findViewById(R.id.notesContainer_tvSubtitle)
        val noteContent: TextView = itemView.findViewById(R.id.notesContainer_tvNoteContent)
        val date: TextView = itemView.findViewById(R.id.notesContainer_tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_list_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = noteList[position].title
        holder.subTitle.text = noteList[position].subtitle
        holder.noteContent.text = noteList[position].content
        holder.date.text = noteList[position].timestamp?.toDate().toString()
    }

    override fun getItemCount(): Int {
       return noteList.size
    }

}