package com.example.funDoNotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funDoNotes.model.User
import com.example.funDoNotes.model.UserAuthListener
import com.example.funDoNotes.model.UserAuthService

class FetchProfileInfoViewModel(val userAuthService: UserAuthService): ViewModel() {

    private var fetchDataStatus = MutableLiveData<UserAuthListener>()
    var _fetchDataStatus: LiveData<UserAuthListener> = fetchDataStatus

    fun fetchProfileData(user: User){
        userAuthService.fetchUserInfo(user,{
            fetchDataStatus.value = it
        })

    }
}