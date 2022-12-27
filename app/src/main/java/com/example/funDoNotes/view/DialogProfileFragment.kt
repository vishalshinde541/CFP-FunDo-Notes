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
import com.example.funDoNotes.model.User
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_register.*

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

        closeBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                dismiss()
            }

        })

        return view
    }

    private fun readFirestoreData() {
        val docRef = db.collection("user").document(firebaseAuth.currentUser?.uid!!)
        docRef.get()
              .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    fName.text = document.getString("firstName")
                    lName.text = document.getString("lastName")
                    email.text = document.getString("email")

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    companion object {

    }
}