package com.example.funDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funDoNotes.model.AuthListener
import com.example.funDoNotes.model.User
import com.example.funDoNotes.model.UserAuthService

class RegisterViewModel(var userAuthService: UserAuthService): ViewModel() {

    private var userRegisterStatus = MutableLiveData<AuthListener>()
    val _userRegisterStatus:LiveData<AuthListener> = userRegisterStatus

    fun registerUSer(user: User){
        userAuthService.userRegister(user,{
            userRegisterStatus.value = it
        })
    }

}