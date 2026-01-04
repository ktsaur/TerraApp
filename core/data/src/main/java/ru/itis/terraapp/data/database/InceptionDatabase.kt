package ru.itis.terraapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.itis.terraapp.data.database.dao.UserDao
import ru.itis.terraapp.data.database.entity.UserEntity
import javax.inject.Inject

@Database(
    entities = [UserEntity::class],
    version = 1
)
//@TypeConverters(CurrentTempConverter::class, ForecastConverter::class)
abstract class InceptionDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}