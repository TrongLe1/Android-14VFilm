package com.example.a14vfilm.models

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sin

data class Film (
    var id: String,
    var seller: String,
    var name: String,
    var description: String,
    var rate: Float,
    var length: Int,
    var country: String,
    var datePublished: Date,
    var price: Int,
    var quantity: Int,
    var dateUpdated: Date,
    var image: String,
    var trailer: String,
    var genre: ArrayList<String>
) : Serializable