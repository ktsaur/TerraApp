package ru.itis.terraapp.data.database.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.terraapp.data.database.dao.UserDao
import ru.itis.terraapp.data.util.toEntity
import ru.itis.terraapp.data.util.toUser
import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val ioDispatchers: CoroutineDispatcher
): UserRepository {

    override suspend fun insertUser(user: User): Int {
        return withContext(ioDispatchers) {
            userDao.insertUser(user = user.toEntity()).toInt()
        }
    }

    override suspend fun deleteUser(userId: Int) {
        withContext(ioDispatchers) {
            val user = userDao.getUserById(userId).toUser()
            userDao.deleteUser(user = user.toEntity())
        }
    }

    override suspend fun getUserById(id: Int): User {
        return withContext(ioDispatchers) {
            userDao.getUserById(id).toUser()
        }
    }

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return withContext(ioDispatchers) {
            userDao.getUserByEmailAndPassword(email = email, password = password)?.toUser()
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return withContext(ioDispatchers) {
            userDao.getUserByEmail(email = email)?.toUser()
        }
    }

    override suspend fun getAllEmails(): List<String>? {
        return withContext(ioDispatchers) {
            userDao.getAllEmails()
        }
    }
}