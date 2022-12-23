package com.example.funDoNotes.model

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserAuthService {

    private lateinit var sEmail: String
    private lateinit var sFirstName: String
    private lateinit var sLastName: String
    private lateinit var sPassword: String

    private var database = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    init {
        initService()
    }

    private fun initService() {

        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun userRegister(user: User, listener: (AuthListener)->Unit){

        firebaseAuth.createUserWithEmailAndPassword(user.email,
            user.password).addOnCompleteListener() {
                if (it.isSuccessful){
                    saveUserDataToFirebase(user, listener)
                    listener(AuthListener(true, "User Registered successfully"))
                }else{
                    listener(AuthListener(false, "User not Registered"))
                }
            }

    }

    fun  userLogin(user: User, listener: (AuthListener)-> Unit){

        firebaseAuth.signInWithEmailAndPassword(user.username,
            user.password).addOnCompleteListener() {
            if (it.isSuccessful){
                listener(AuthListener(true, "User login successfully"))
            }else{
                listener(AuthListener(false, "User login filed"))
            }
        }

    }

    fun saveUserDataToFirebase(user: User, listener: (AuthListener) -> Unit){

        val userMap = hashMapOf(
            "email" to user.email,
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "password" to user.password
        )

        database.collection("user").add(userMap)
            .addOnCompleteListener {
                if (it.isSuccessful){

                    listener(AuthListener(true, "Data added successfully"))
                }else{
                    listener(AuthListener(false, "Filed to add data"))
                }

            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun fetchUserInfo(listener: (UserAuthListener)-> Unit){

        val uid = firebaseAuth.currentUser?.uid

        var user = User(
            "",
            "",
            "",
            ""

        )
        val docref = database.collection("user").document("uid")
            docref.get().addOnCompleteListener {
            if (it.isSuccessful){
                 user = User(
                     it.result.getString("email").toString(),
                     it.result.getString("firstName").toString(),
                     it.result.getString("lastName").toString()
                     )
                listener(UserAuthListener(true, "Data fetch successfully", user))
            }else{
                listener(UserAuthListener(false, "Data fetch filed", user))
            }
        }
    }





}