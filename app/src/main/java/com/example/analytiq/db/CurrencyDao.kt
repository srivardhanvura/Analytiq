package com.example.analytiq.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CurrencyDao {

    @Insert
    fun insertCurrency(currency:CurrencyEntity)

    @Delete
    fun deleteCurrency(currency: CurrencyEntity)

    @Query("SELECT * FROM singleCurrency")
    fun getAllCurrency():List<CurrencyEntity>
}