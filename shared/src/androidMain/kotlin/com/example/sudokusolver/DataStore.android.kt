package com.example.sudokusolver

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import com.example.sudokusolver.datastore.createDataStore as createCommonDataStore
import com.example.sudokusolver.datastore.dataStoreFileName


fun createDataStore(context: Context): DataStore<Preferences> = createCommonDataStore(
    storage = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { context.filesDir.resolve(dataStoreFileName) }
    )
)
