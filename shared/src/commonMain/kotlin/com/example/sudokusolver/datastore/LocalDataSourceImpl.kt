package com.example.sudokusolver.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.serialization.internal.throwMissingFieldException

class LocalDataSourceImpl(private val datastore: DataStore<Preferences>) : LocalDataSource {

    override suspend fun save(key: String, value: String): Unit {
        val prefKey : Preferences.Key<String> = stringPreferencesKey(key)
        datastore.edit { it[prefKey] = value }

    }

    override suspend fun get(key: String): String {
        val prefKey : Preferences.Key<String> = stringPreferencesKey(key)
        val returnedVal =
            datastore.data.first()[prefKey] ?: throw RuntimeException("Key not found")
        return returnedVal
    }

    override suspend fun clear(key: String): Unit {
        val prefKey : Preferences.Key<String> = stringPreferencesKey(key)
        datastore.edit {it.remove(prefKey)}
    }
}