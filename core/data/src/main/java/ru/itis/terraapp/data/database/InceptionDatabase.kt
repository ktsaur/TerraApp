package ru.itis.terraapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.itis.terraapp.data.database.converters.CurrentTempConverter
import ru.itis.terraapp.data.database.converters.ForecastConverter
import ru.itis.terraapp.data.database.dao.QueryHistoryDao
import ru.itis.terraapp.data.database.dao.UserDao
import ru.itis.terraapp.data.database.dao.WeatherApiDao
import ru.itis.terraapp.data.database.entity.QueryHistoryEntity
import ru.itis.terraapp.data.database.entity.UserEntity
import ru.itis.terraapp.data.database.entity.WeatherApiEntity
import javax.inject.Inject

@Database(
    entities = [WeatherApiEntity::class, QueryHistoryEntity::class, UserEntity::class],
    version = 2
)
@TypeConverters(CurrentTempConverter::class, ForecastConverter::class)
abstract class InceptionDatabase: RoomDatabase() {
    abstract val weatherApiDao: WeatherApiDao
    abstract val queryHistoryDao: QueryHistoryDao
    abstract val userDao: UserDao
}