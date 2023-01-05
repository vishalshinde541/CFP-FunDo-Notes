package com.example.funDoNotes.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DialogProfileFragment : DialogFragment() {

    private lateinit var email: TextView
    private lateinit var fName: TextView
    private lateinit var lName: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog_profile, container, false)
        var closeBtn = view.findViewById<Button>(R.id.dialog_dismissBtn)

        email = view.findViewById(R.id.dialog_email) as TextView
        fName = view.findViewById(R.id.dialog_fname) as TextView
        lName = view.findViewById(R.id.dialog_lname) as TextView
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        readFirestoreData()

        closeBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }

        })

        return view
    }

    private fun readFirestoreData() {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = db.collection("user").document(userId)
        docRef.get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    fName.text = it.result.getString("firstName")
                    lName.text = it.result.getString("lastName")
                    email.text = it.result.getString("email")

                } else {
                    Log.d(TAG, "No such document")
                }
            }

    }

    companion object {

    }
}