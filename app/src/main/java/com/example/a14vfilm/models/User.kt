package com.example.a14vfilm.models

import android.app.Application
import java.io.Serializable

data class User(
    var id: String,
    var email: String,
    var password: String,
    var name: String,
    var address: String,
    var phone: String,
    var image: String,
    var status: Boolean,
    var role: Int //0 buyer, 1 seller, 2 admin
): Serializable {
//    override fun toString(): String {
//        return "$id $name $datePublished"
//    }
}



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