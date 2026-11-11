package com.example.sudokusolver


interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val showSplash: Boolean