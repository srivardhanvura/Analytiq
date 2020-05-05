package com.example.analytiq.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "singleCurrency")
data class CurrencyEntity(
    @PrimaryKey val index: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val code: String,
    @ColumnInfo val rate: String
)