package ru.itis.terraapp.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.domain.repositories.ForecastRepository
import ru.itis.terraapp.domain.usecase.forecast.GetForecastByCityNameUseCase

internal class GetForecastByCityNameUseCaseTest {

    private lateinit var forecastRepository: ForecastRepository
    private lateinit var useCase: GetForecastByCityNameUseCase

    private val fakeCity = "Kazan"

    private val fakeForecastList = listOf(
        Forecast(
            dt = "test dt",
            temp = 5f,
            feelsLike = 5f,
            tempMin = 5f,
            tempMax = 5f,
            mainDesc = "main test desc",
            description = "test desc"
        ),
        Forecast(
            dt = "test dt2",
            temp = 2f,
            feelsLike = 2f,
            tempMin = 2f,
            tempMax = 2f,
            mainDesc = "main test desc2",
            description = "test desc2"
        )
    )

    @Before
    fun setUp() {
        forecastRepository = mockk()
        useCase = GetForecastByCityNameUseCase(forecastRepository)
    }

    @Test
    fun `should return forecast list when repository returns forecast successfully`() = runTest {
        // GIVEN
        coEvery { forecastRepository.getForecastByCityName(city = fakeCity) } returns fakeForecastList

        // WHEN
        val result = useCase(fakeCity)

        // THEN
        assertEquals(fakeForecastList, result)
    }

    @Test
    fun `should propagate exception when repository fails to get forecast`() = runTest {
        // GIVEN
        val exception = RuntimeException("Город не найден или ошибка сети")
        coEvery { forecastRepository.getForecastByCityName(city = fakeCity) } throws exception

        // WHEN & THEN
        val thrownException = runCatching { useCase(fakeCity) }.exceptionOrNull()

        assert(thrownException != null)
        assertEquals("Город не найден или ошибка сети", thrownException?.message)
    }
}