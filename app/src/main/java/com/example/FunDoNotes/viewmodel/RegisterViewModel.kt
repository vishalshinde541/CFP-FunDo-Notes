package com.example.FunDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.FunDoNotes.model.AuthListener
import com.example.FunDoNotes.model.User
import com.example.FunDoNotes.model.UserAuthService

class RegisterViewModel(var userAuthService: UserAuthService): ViewModel() {

    private var userRegisterStatus = MutableLiveData<AuthListener>()
    val _userRegisterStatus:LiveData<AuthListener> = userRegisterStatus

    fun registerUSer(user: User){
        userAuthService.userRegister(user,{
            userRegisterStatus.value = it
        })
    }

}