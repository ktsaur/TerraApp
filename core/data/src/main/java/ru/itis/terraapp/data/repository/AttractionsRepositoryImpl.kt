package ru.itis.terraapp.data.repository

import ru.itis.terraapp.data.remote.AttractionsApi
import ru.itis.terraapp.data.remote.pojo.AttractionsResponse
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.AttractionsRepository
import javax.inject.Inject

class AttractionsRepositoryImpl @Inject constructor(
    private val attractionsApi: AttractionsApi
) : AttractionsRepository {
    
    override suspend fun getAttractionsByCityName(city: String): List<Attraction> {
        val response = attractionsApi.getAttractionsByCityName(city)
            ?: throw IllegalStateException("Attractions not found for city: $city")
        
        return response.toDomain()
    }
    
    private fun AttractionsResponse.toDomain(): List<Attraction> {
        return attractions?.mapNotNull { item ->
            if (item.id == null || item.name == null) return@mapNotNull null
            
            Attraction(
                id = item.id,
                name = item.name,
                description = item.description ?: "",
                rating = item.rating ?: 0f,
                address = item.address ?: "",
                imageUrl = item.imageUrl ?: "",
                category = item.category ?: "",
                latitude = item.latitude ?: 0.0,
                longitude = item.longitude ?: 0.0
            )
        } ?: emptyList()
    }
}

