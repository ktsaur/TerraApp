package ru.itis.terraapp.domain.repositories

import ru.itis.terraapp.domain.model.User

interface UserRepository {
    suspend fun insertUser(user: User) : Int
    suspend fun deleteUser(userId: Int)
    suspend fun getUserById(id: Int): User
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getAllEmails(): List<String>?
}