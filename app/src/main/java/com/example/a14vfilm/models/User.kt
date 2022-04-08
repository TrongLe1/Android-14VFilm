package com.example.a14vfilm.models

import android.app.Application

data class User(
    var id: String,
    var email: String,
    var password: String,
    var name: String,
    var address: String,
    var phone: String,
    var image: String
)

/*
data class UserLogin(
    var id: String,
    var email: String,
    var name: String,
    var address: String,
    var phone: String,
    var image: String
): Application()
*/

class UserLogin: Application() {
    companion object {
        var info: User? = null
    }
}