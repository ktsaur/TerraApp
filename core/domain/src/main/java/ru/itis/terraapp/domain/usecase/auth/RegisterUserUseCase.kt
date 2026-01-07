package ru.itis.terraapp.domain.usecase.auth

import org.mindrot.jbcrypt.BCrypt
import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Int {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val newUser = User(
            username = username,
            email = email,
            password = hashedPassword
        )
        return userRepository.insertUser(newUser)
    }
}
