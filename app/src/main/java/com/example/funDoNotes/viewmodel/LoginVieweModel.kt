package com.example.funDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funDoNotes.model.AuthListener
import com.example.funDoNotes.model.User
import com.example.funDoNotes.model.UserAuthService

class LoginVieweModel(var userAuthService: UserAuthService): ViewModel() {

    private var userLoinStatus = MutableLiveData<AuthListener>()
    val _userLoginStatus:LiveData<AuthListener> = userLoinStatus

    fun loginUser(user: User){
        userAuthService.userLogin(user,{
            userLoinStatus.value = it
        })
    }
}