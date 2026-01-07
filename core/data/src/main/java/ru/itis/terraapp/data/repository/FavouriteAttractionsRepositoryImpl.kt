package ru.itis.terraapp.data.repository

import ru.itis.terraapp.data.database.entity.FavouriteAttractionEntity
import ru.itis.terraapp.data.database.repository.FavouriteAttractionRepository
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.FavouriteAttractionsRepository
import javax.inject.Inject

class FavouriteAttractionsRepositoryImpl @Inject constructor(
    private val favouriteAttractionRepository: FavouriteAttractionRepository
) : FavouriteAttractionsRepository {

    override suspend fun addToFavourites(attraction: Attraction, userId: Int) {
        favouriteAttractionRepository.addToFavourites(attraction.toEntity(userId))
    }

    override suspend fun removeFromFavourites(attractionId: String, userId: Int) {
        favouriteAttractionRepository.removeFromFavourites(attractionId, userId)
    }

    override suspend fun getFavourites(userId: Int): List<Attraction> {
        return favouriteAttractionRepository.getFavourites(userId).map { it.toDomain() }
    }

    override suspend fun isFavourite(attractionId: String, userId: Int): Boolean {
        return favouriteAttractionRepository.isFavourite(attractionId, userId)
    }

    private fun Attraction.toEntity(userId: Int): FavouriteAttractionEntity {
        return FavouriteAttractionEntity(
            attractionId = id,
            userId = userId,
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


