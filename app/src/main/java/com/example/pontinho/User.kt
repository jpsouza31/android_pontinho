package com.example.pontinho

class User(
    val id: Int,
    val name: String,
    var score: Int,
    val photo: Int?,
    var isGameOver: Boolean = false
)