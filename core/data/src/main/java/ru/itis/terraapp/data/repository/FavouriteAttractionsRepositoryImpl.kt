package ru.itis.terraapp.data.repository

import ru.itis.terraapp.data.database.entity.FavouriteAttractionEntity
import ru.itis.terraapp.data.database.repository.FavouriteAttractionRepository
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.FavouriteAttractionsRepository
import javax.inject.Inject

class FavouriteAttractionsRepositoryImpl @Inject constructor(
    private val favouriteAttractionRepository: FavouriteAttractionRepository
) : FavouriteAttractionsRepository {

    override suspend fun addToFavourites(attraction: Attraction) {
        favouriteAttractionRepository.addToFavourites(attraction.toEntity())
    }

    override suspend fun removeFromFavourites(attractionId: String) {
        favouriteAttractionRepository.removeFromFavourites(attractionId)
    }

    override suspend fun getFavourites(): List<Attraction> {
        return favouriteAttractionRepository.getFavourites().map { it.toDomain() }
    }

    override suspend fun isFavourite(attractionId: String): Boolean {
        return favouriteAttractionRepository.isFavourite(attractionId)
    }

    private fun Attraction.toEntity(): FavouriteAttractionEntity {
        return FavouriteAttractionEntity(
            attractionId = id,
            name = name,
            description = description,
            rating = rating,
            address = address,
            imageUrl = imageUrl,
            category = category,
            latitude = latitude,
            longitude = longitude
        )
    }

    private fun FavouriteAttractionEntity.toDomain(): Attraction {
        return Attraction(
            id = attractionId,
            name = name,
            description = description,
            rating = rating,
            address = address,
            imageUrl = imageUrl,
            category = category,
            latitude = latitude,
            longitude = longitude
        )
    }
}


