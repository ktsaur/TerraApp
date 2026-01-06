package ru.itis.terraapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_attraction")
data class FavouriteAttractionEntity(
    @PrimaryKey
    @ColumnInfo(name = "attraction_id")
    val attractionId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
)


