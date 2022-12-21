package com.example.FunDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.FunDoNotes.model.AuthListener
import com.example.FunDoNotes.model.User
import com.example.FunDoNotes.model.UserAuthService

class LoginVieweModel(var userAuthService: UserAuthService): ViewModel() {

    private var userLoinStatus = MutableLiveData<AuthListener>()
    val _userLoginStatus:LiveData<AuthListener> = userLoinStatus

    fun loginUser(user: User){
        userAuthService.userLogin(user,{
            userLoinStatus.value = it
        })
    }
}