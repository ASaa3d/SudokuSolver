package com.example.sudokusolver.sudoku.data.remote.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDto(
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val userName: String? = null,
    val imgUrl: String,
)
