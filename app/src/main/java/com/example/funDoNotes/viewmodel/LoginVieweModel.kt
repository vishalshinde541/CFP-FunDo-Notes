package com.example.funDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funDoNotes.model.ApiAuthListner
import com.example.funDoNotes.model.AuthListener
import com.example.funDoNotes.model.User
import com.example.funDoNotes.model.UserAuthService

class LoginVieweModel(var userAuthService: UserAuthService): ViewModel() {

    private var userLoginStatus = MutableLiveData<AuthListener>()
    val _userLoginStatus:LiveData<AuthListener> = userLoginStatus

    private var loginStatus = MutableLiveData<ApiAuthListner>()
    val _loginStatus:LiveData<ApiAuthListner> = loginStatus

    fun loginUser(user: User){
        userAuthService.userLogin(user,{
            userLoginStatus.value = it
        })
    }



    fun loginWithApi(email : String, password : String){
        userAuthService.restApiLogin(email, password,{
            if (it.status){
                loginStatus.value = it
            }
        })

    }
}