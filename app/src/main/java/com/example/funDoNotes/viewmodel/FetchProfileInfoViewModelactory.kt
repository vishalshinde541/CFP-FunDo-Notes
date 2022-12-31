package com.example.funDoNotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.funDoNotes.model.UserAuthService

class FetchProfileInfoViewModelactory(val userAuthService: UserAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FetchProfileInfoViewModel(userAuthService) as T
    }

}