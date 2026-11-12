package com.example.sudokusolver.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.dsl.module

val webModule = module {
    single<DataStore<Preferences>> { createDataStore() }



}

