package com.example.FunDoNotes.model

import com.google.firebase.auth.FirebaseAuth

class UserAuthService {


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



}