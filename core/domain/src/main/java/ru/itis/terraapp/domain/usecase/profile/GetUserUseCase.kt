package ru.itis.terraapp.domain.usecase.profile

import org.mindrot.jbcrypt.BCrypt
import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(userId: Int): User {
        return userRepository.getUserById(id = userId)
    }
}