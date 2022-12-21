package com.example.FunDoNotes.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private lateinit var etRegisteredEmail: EditText
    private lateinit var resetBtn: Button
    private lateinit var fAuth: FirebaseAuth

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
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        resetBtn = view.findViewById(R.id.resetBtn)
        fAuth = FirebaseAuth.getInstance()
        etRegisteredEmail = view.findViewById(R.id.etRegisteredEmail)

        resetBtn.setOnClickListener {
             var setEmail = etRegisteredEmail.text.toString()
            fAuth.sendPasswordResetEmail(setEmail)
                .addOnSuccessListener {
                    Toast.makeText(context, "Please check you email", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }

        }
        return view
    }

}