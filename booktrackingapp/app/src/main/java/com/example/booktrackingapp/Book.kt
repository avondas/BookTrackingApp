package com.example.booktrackingapp

data class Book(
    val id : Int,
    val title: String,
    val author: String,
    val read: Boolean,
    val series: String?,
    val image: Int
)
