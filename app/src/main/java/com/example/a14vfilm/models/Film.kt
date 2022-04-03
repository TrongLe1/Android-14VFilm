package com.example.a14vfilm.models

import java.util.*

data class Film (
    var id: Int,
    var sellerId: String,
    var name: String,
    var description: String,
    var rate: Float,
    var length: Int,
    var country: String,
    var datePublished: Date,
    var price: Int,
    var quantity: Int,
    var dateUpdated: Date
)