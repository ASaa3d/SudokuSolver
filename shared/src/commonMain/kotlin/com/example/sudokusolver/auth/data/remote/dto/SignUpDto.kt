package com.example.sudokusolver.auth.data.remote.dto

import com.example.sudokusolver.auth.domain.model.SignUpData
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    val userName: String,
    val email: String,
    val password: String,
)

fun SignUpData.toDto(): SignUpDto {
    return SignUpDto(
        userName=this.userName,
        email=this.email,
        password=this.password,
    )
}
