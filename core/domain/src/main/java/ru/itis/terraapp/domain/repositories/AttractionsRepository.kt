package ru.itis.terraapp.domain.repositories

import ru.itis.terraapp.domain.model.Attraction

interface AttractionsRepository {
    suspend fun getAttractionsByCityName(city: String): List<Attraction>
}

