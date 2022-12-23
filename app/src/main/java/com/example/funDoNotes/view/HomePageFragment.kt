package com.example.funDoNotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginandregistrationwithfragment.R
import com.google.android.material.floatingactionbutton.FloatingActionButton



class HomePageFragment : Fragment(R.layout.fragment_home_page) {

    private lateinit var floatingActionBtn : FloatingActionButton

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

        floatingActionBtn = view.findViewById(R.id.floatingActionBtn)

        floatingActionBtn.setOnClickListener {

            Toast.makeText(context, "Floating Action Btn clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }


}