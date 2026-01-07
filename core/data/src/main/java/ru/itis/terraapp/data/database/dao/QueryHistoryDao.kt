package ru.itis.terraapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.terraapp.data.database.entity.QueryHistoryEntity

@Dao
interface QueryHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuery(queryHistoryEntity: QueryHistoryEntity)

    @Query("SELECT timestamp FROM queryHistory WHERE city = :city ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastQueryForCity(city: String): Long?

    @Query("SELECT COUNT(*) FROM queryHistory WHERE timestamp > :start AND timestamp < :end")
    suspend fun countQueryBetween(start: Long, end: Long) : Int

}