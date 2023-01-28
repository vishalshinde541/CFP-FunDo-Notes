package com.example.funDoNotes.networkApi

object Constant {
    lateinit var uId : String
    var constant : Constant? = null

    fun getInstance() : Constant? {

        if (constant != null){

            constant = Constant
        }

        return constant
    }

    fun setUserId(userId : String){
        this.uId = userId
    }

}