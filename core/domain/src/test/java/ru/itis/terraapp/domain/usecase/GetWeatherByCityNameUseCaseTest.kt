package ru.itis.terraapp.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import ru.itis.terraapp.domain.model.Weather
import ru.itis.terraapp.domain.repositories.WeatherRepository
import ru.itis.terraapp.domain.usecase.forecast.GetWeatherByCityNameUseCase

internal class GetWeatherByCityNameUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var useCase: GetWeatherByCityNameUseCase

    private val fakeCity = "Moscow"
    private val fakeWeather = Weather(
        currentTemp = 2f,
        weatherDescription = "testdescription",
    )

    @Before
    fun setUp() {
        weatherRepository = mockk()
        useCase = GetWeatherByCityNameUseCase(weatherRepository)
    }

    @Test
    fun `should return weather when repository returns weather successfully`() = runTest {
        // GIVEN
        coEvery { weatherRepository.getWeatherByCityName(city = fakeCity) } returns fakeWeather

        // WHEN
        val result = useCase(fakeCity)

        // THEN
        assertEquals(fakeWeather, result)
    }

    @Test
    fun `should propagate exception when repository fails to get weather`() = runTest {
        // GIVEN
        val exception = RuntimeException("Город не найден или ошибка сети")
        coEvery { weatherRepository.getWeatherByCityName(city = fakeCity) } throws exception

        // WHEN & THEN
        val thrownException = runCatching { useCase(fakeCity) }.exceptionOrNull()

        assert(thrownException != null)
        assertEquals("Город не найден или ошибка сети", thrownException?.message)
    }
}