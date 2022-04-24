package com.example.a14vfilm.models

import java.util.*

data class Transaction (
    var id: String,
    var user: String,
    var film: String,
    var rentDate: Date,
    var expired: Date,
    var total: Long,
    var rate: Float,
    var type: Boolean,
    var comment: String
)