package com.example.sudokusolver.di.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.sudokusolver.di.createDataStore
import org.koin.dsl.module

val webModule = module {
    single<DataStore<Preferences>> { createDataStore() }


}

