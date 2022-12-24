package com.example.funDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funDoNotes.model.User
import com.example.funDoNotes.model.UserAuthListener
import com.example.funDoNotes.model.UserAuthService

class ProfileViewModel(var userAuthService: UserAuthService): ViewModel() {
    private var userFetchDataStatus = MutableLiveData<UserAuthListener>()
    val _userFetchDataStatus: LiveData<UserAuthListener> = userFetchDataStatus


    fun fetchUserData() {
        userAuthService.fetchUserInfo{
            userFetchDataStatus.value = it
        }
    }
}


