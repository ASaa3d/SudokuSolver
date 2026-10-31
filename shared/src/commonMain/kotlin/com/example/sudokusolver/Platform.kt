package com.example.sudokusolver

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform