package com.example.sudokusolver.splash.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val userName: String,
    val email: String = "",
    val imgUrl: String? = null,
    val creationDate: String? = null,
    val streak: Int = 0,
    val totalScore: Int = 0
)
