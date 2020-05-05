package com.example.analytiq.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class CurrencyDatabase:RoomDatabase() {

    abstract fun getDao(): CurrencyDao

}