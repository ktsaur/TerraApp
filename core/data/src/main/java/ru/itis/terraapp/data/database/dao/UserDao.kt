package ru.itis.terraapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.terraapp.data.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user: UserEntity): Long

    @Delete
    fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE user_id = :id")
    fun getUserById(id: Int): UserEntity

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT email FROM user")
    fun getAllEmails(): List<String>?

}