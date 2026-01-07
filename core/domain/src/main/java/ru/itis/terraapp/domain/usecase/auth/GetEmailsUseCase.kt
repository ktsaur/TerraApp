package ru.itis.terraapp.domain.usecase.auth

import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Inject

class GetEmailsUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(): List<String> {
        return userRepository.getAllEmails()
    }
}