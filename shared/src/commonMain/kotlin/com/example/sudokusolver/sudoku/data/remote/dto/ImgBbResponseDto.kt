package com.example.sudokusolver.sudoku.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImgBbResponseDto(
    val data: ImgBbDataDto? = null,
    val success: Boolean? = null,
    val status: Int? = null,
)

@Serializable
data class ImgBbDataDto(
    val id: String? = null,
    val title: String? = null,
    @SerialName("url_viewer") val urlViewer: String? = null,
    val url: String? = null,
    @SerialName("display_url") val displayUrl: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val size: Int? = null,
    val time: Long? = null,
    val expiration: Long? = null,
)
