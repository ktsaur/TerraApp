package ru.itis.terraapp.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.itis.terraapp.domain.repositories.UserRepository
import ru.itis.terraapp.domain.usecase.profile.DeleteUserUseCase

internal class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: DeleteUserUseCase

    private val fakeUserId = 42

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true) // relaxed = true, чтобы не бросало исключение при не замоканных вызовах
        useCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `should delete user successfully when repository succeeds`() = runTest {
        // GIVEN - ничего мокать не нужно, т.к. метод возвращает Unit и не бросает исключение по умолчанию

        // WHEN
        useCase(fakeUserId)

        // THEN
        coVerify(exactly = 1) { userRepository.deleteUser(userId = fakeUserId) }
    }

    @Test
    fun `should propagate exception when repository fails to delete user`() = runTest {
        // GIVEN
        val exception = RuntimeException("Не удалось удалить пользователя")
        coEvery { userRepository.deleteUser(userId = fakeUserId) } throws exception

        // WHEN & THEN
        val thrownException = runCatching { useCase(fakeUserId) }.exceptionOrNull()

        assert(thrownException != null)
        assert(thrownException?.message == "Не удалось удалить пользователя")
        coVerify(exactly = 1) { userRepository.deleteUser(userId = fakeUserId) }
    }
}