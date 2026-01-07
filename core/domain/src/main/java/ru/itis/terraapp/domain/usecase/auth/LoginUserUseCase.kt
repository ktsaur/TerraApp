package ru.itis.terraapp.domain.usecase.auth

import org.mindrot.jbcrypt.BCrypt
import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        val user = userRepository.getUserByEmail(email = email)
        
        return if (user != null) {
            val storedHash = user.password
            if (BCrypt.checkpw(password, storedHash)) {
                LoginResult.Success(user)
            } else {
                LoginResult.WrongPassword
            }
        } else {
            LoginResult.UserNotFound
        }
    }
}

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    object WrongPassword : LoginResult()
    object UserNotFound : LoginResult()
}

