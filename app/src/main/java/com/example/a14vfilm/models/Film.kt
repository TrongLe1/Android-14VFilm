package com.example.a14vfilm.models

import java.util.*

data class Film (
    var id: Int,
    var sellerId: String,
    var name: String,
    var description: String,
    var like: Int,
    var dislike: Int,
    var type: String,
    var length: Int,
    var episode: Int,
    var country: String,
    var datePublished: Date,
    var price: Double,
    var quantity: Int,
    var dateUpdated: Date
)