package com.example.sudokusolver.splash.domain.model

data class ParsedUrl(
    val path: String,
    val queryParams: Map<String, String>
)
