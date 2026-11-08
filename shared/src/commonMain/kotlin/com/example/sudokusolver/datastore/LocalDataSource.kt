package com.example.sudokusolver.datastore


interface LocalDataSource {
    suspend fun save(key:String,value:String) : Unit
    suspend fun get(key:String) : String
    suspend fun clear(key:String) : Unit
}