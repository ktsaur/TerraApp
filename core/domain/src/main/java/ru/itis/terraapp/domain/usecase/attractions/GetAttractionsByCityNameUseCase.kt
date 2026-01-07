package ru.itis.terraapp.domain.usecase.attractions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.AttractionsRepository
import javax.inject.Inject

class GetAttractionsByCityNameUseCase @Inject constructor(
    private val attractionsRepository: AttractionsRepository
) {
    suspend operator fun invoke(city: String): List<Attraction> {
        return withContext(Dispatchers.IO) {
            attractionsRepository.getAttractionsByCityName(city = city)
        }
    }
}

