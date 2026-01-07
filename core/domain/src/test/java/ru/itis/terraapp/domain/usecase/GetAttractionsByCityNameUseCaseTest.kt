package ru.itis.terraapp.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.AttractionsRepository
import ru.itis.terraapp.domain.usecase.attractions.GetAttractionsByCityNameUseCase

internal class GetAttractionsByCityNameUseCaseTest {

    private lateinit var attractionsRepository: AttractionsRepository
    private lateinit var useCase: GetAttractionsByCityNameUseCase

    private val fakeCity = "Paris"

    private val fakeAttractionsList = listOf(
        Attraction(
            id = "attr1",
            name = "Red squere",
            description = "desc",
            rating =  5f,
            address = "test address",
            imageUrl = "test url",
            category = "test category",
            latitude =  15.56,
            longitude = 18.23
        ),
        Attraction(
            id = "attr2",
            name = "Red squere2",
            description = "desc2",
            rating =  13f,
            address = "test address2",
            imageUrl = "test url2",
            category = "test category2",
            latitude =  19.56,
            longitude = 22.23
        )
    )

    @Before
    fun setUp() {
        attractionsRepository = mockk()
        useCase = GetAttractionsByCityNameUseCase(attractionsRepository)
    }

    @Test
    fun `should return attractions list when repository returns attractions successfully`() = runTest {
        // GIVEN
        coEvery { attractionsRepository.getAttractionsByCityName(city = fakeCity) } returns fakeAttractionsList

        // WHEN
        val result = useCase(fakeCity)

        // THEN
        assertEquals(fakeAttractionsList, result)
    }

    @Test
    fun `should propagate exception when repository fails to get attractions`() = runTest {
        // GIVEN
        val exception = RuntimeException("Достопримечательности не найдены или ошибка сети")
        coEvery { attractionsRepository.getAttractionsByCityName(city = fakeCity) } throws exception

        // WHEN & THEN
        val thrownException = runCatching { useCase(fakeCity) }.exceptionOrNull()

        assert(thrownException != null)
        assertEquals("Достопримечательности не найдены или ошибка сети", thrownException?.message)
    }
}