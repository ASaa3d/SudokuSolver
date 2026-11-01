package com.example.sudokusolver.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module

actual val platformModule: Module = androidModule