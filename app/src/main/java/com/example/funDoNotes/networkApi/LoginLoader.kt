package com.example.funDoNotes.networkApi

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginLoader {

    fun getLoginDone(listner: LoginListner, email : String, password : String){
        FundoClient.getInstance()?.getMyApi()?.loginFundoUser(LoginRequest(email, password, true))
            ?.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let { listner.onLogin(it, true) }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                   listner.onLogin(null, false)
                }


            })
    }
}