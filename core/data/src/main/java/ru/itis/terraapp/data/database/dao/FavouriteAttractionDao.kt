package ru.itis.terraapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.terraapp.data.database.entity.FavouriteAttractionEntity

@Dao
interface FavouriteAttractionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(attraction: FavouriteAttractionEntity)

    @Delete
    suspend fun deleteFavourite(attraction: FavouriteAttractionEntity)

    @Query("DELETE FROM favourite_attraction WHERE attraction_id = :attractionId")
    suspend fun deleteFavouriteById(attractionId: String)

    @Query("SELECT * FROM favourite_attraction")
    suspend fun getAllFavourites(): List<FavouriteAttractionEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_attraction WHERE attraction_id = :attractionId)")
    suspend fun isFavourite(attractionId: String): Boolean
}


