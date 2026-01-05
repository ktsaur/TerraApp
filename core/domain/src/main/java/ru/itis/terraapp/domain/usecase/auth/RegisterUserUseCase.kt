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
    ): RegisterResult {
        // Валидация
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            return RegisterResult.EmptyFields
        }
        
        if (!email.contains("@")) {
            return RegisterResult.InvalidEmail
        }
        
        if (password.length < 6 || !isValidPassword(password)) {
            return RegisterResult.InvalidPassword
        }
        
        // Проверка на существующий email
        val existingEmails = userRepository.getAllEmails()
        if (existingEmails?.contains(email) == true) {
            return RegisterResult.EmailTaken
        }
        
        // Хеширование пароля
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        
        // Проверка на существующего пользователя
        val existingUser = userRepository.getUserByEmailAndPassword(
            email = email,
            password = hashedPassword
        )
        if (existingUser != null) {
            return RegisterResult.UserAlreadyExists
        }
        
        // Создание нового пользователя
        val newUser = User(
            username = username,
            email = email,
            password = hashedPassword
        )
        val userId = userRepository.insertUser(newUser)
        
        return RegisterResult.Success(userId)
    }
    
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d).+$"
        return password.matches(passwordPattern.toRegex())
    }
}

sealed class RegisterResult {
    data class Success(val userId: Int) : RegisterResult()
    object EmptyFields : RegisterResult()
    object InvalidEmail : RegisterResult()
    object InvalidPassword : RegisterResult()
    object EmailTaken : RegisterResult()
    object UserAlreadyExists : RegisterResult()
}

