package com.example.booktrackingapp

class Book(
    val title: String,
    val author: String,
    val read: Boolean,
)

class Series(
    val name: String,
    val books: List<Book>,
)
