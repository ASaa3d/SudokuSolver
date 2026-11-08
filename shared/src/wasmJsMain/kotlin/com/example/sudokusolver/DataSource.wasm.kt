package com.example.sudokusolver

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.WebLocalStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import com.example.sudokusolver.datastore.createDataStore
import com.example.sudokusolver.datastore.dataStoreFileName


fun createDataStore(): DataStore<Preferences> = createDataStore(
    storage = WebLocalStorage(
        serializer = PreferencesSerializer,
        name = dataStoreFileName
    )
)
