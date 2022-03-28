package com.example.a14vfilm.models

import java.util.*

data class Transaction (
    var user: String,
    var film: Int,
    var rentDate: Date,
    var expired: Date,
    var total: Double,
    var name: String
)