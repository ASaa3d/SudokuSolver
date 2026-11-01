package com.example.sudokusolver

import android.app.Application
import com.example.sudokusolver.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            it.androidLogger()
            it.androidContext(this@MyApp)
        }
    }
}