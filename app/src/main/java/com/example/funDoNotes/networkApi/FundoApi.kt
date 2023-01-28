package com.example.funDoNotes.networkApi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FundoApi {

    @POST("./accounts:signInWithPassword?key=AIzaSyAp8HBEtj8jjTEgSbrHSO0UsxdqibW14oU")
    fun loginFundoUser(@Body request : LoginRequest) : Call<LoginResponse>
}