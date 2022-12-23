package com.example.funDoNotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.funDoNotes.model.User
import com.example.funDoNotes.viewmodel.ProfileViewModel
import com.example.loginandregistrationwithfragment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.funDoNotes.model.UserAuthService
import com.example.funDoNotes.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var fAuth: FirebaseAuth
    private var database = Firebase.firestore

    private lateinit var showFName: TextView
    private lateinit var showLName: TextView
    private lateinit var showEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(UserAuthService())).get(
            ProfileViewModel::class.java
        )

        fAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        showFName = view.findViewById(R.id.profile_tvFName)
        showLName = view.findViewById(R.id.profile_tvLName)
        showEmail = view.findViewById(R.id.profile_tvEmail)


        val user = User(
            email = showEmail.text.toString(),
            firstName = showFName.text.toString(),
            lastName = showLName.text.toString(),
        )

        profileViewModel.fetchUserData(user)
        profileViewModel._userFetchDataStatus.observe(viewLifecycleOwner, Observer
        {
            showEmail.text =  it.user.email.toString()
            showFName.text = it.user.firstName.toString()
            })

        return view
    }

}