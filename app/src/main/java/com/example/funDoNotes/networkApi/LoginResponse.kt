package com.example.funDoNotes.networkApi

data class LoginResponse(
    var idToken: String,
    var email: String,
    var emarefreshTokenil: String,
    var expiresIn: String,
    var localId: String,
    var registered: Boolean
) {

}
