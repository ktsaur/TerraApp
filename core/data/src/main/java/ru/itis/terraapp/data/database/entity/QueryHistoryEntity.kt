package ru.itis.terraapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queryHistory")
data class QueryHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
