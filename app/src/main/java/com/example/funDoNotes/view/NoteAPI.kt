package com.example.funDoNotes.view

import com.example.funDoNotes.model.Note
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NoteInterfaceAPI {


    @GET("./accounts:lookup?key=AIzaSyAp8HBEtj8jjTEgSbrHSO0UsxdqibW14oU")
    fun getNotes() : Call<List<Note?>?>
}

object getNoteService{

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    val noteIntance : NoteInterfaceAPI
    init {

        var retrofit = Retrofit.Builder()
            .baseUrl("https://identitytoolkit.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        noteIntance = retrofit.create(NoteInterfaceAPI::class.java)
    }
}