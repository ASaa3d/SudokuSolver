package com.example.sudokusolver

import android.app.Application
import com.example.sudokusolver.di.initKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)

    }
}