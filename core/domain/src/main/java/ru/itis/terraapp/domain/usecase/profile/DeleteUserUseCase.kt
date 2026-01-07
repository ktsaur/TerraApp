package ru.itis.terraapp.domain.usecase.profile

import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int) {
        userRepository.deleteUser(userId = userId)
    }
}