package ru.itis.terraapp.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.repositories.UserRepository
import ru.itis.terraapp.domain.usecase.profile.GetUserUseCase

internal class GetUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetUserUseCase

    private val fakeUserId = 123
    private val fakeUser = User(
        userId = fakeUserId,
        username = "testuser",
        email = "test@example.com",
        password = "testpass"
    )

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetUserUseCase(userRepository)
    }

    @Test
    fun `should return user when repository returns user successfully`() = runTest {
        // GIVEN
        coEvery { userRepository.getUserById(id = fakeUserId) } returns fakeUser

        // WHEN
        val result = useCase(fakeUserId)

        // THEN
        assertEquals(fakeUser, result)
    }

    @Test
    fun `should propagate exception when repository fails to get user`() = runTest {
        // GIVEN
        val exception = RuntimeException("Пользователь не найден")
        coEvery { userRepository.getUserById(id = fakeUserId) } throws exception

        // WHEN & THEN
        val thrownException = runCatching { useCase(fakeUserId) }.exceptionOrNull()

        assert(thrownException != null)
        assertEquals("Пользователь не найден", thrownException?.message)
    }
}