package ru.itis.terraapp.domain.repositories

import ru.itis.terraapp.domain.model.Attraction

interface FavouriteAttractionsRepository {
    suspend fun addToFavourites(attraction: Attraction, userId: Int)
    suspend fun removeFromFavourites(attractionId: String, userId: Int)
    suspend fun getFavourites(userId: Int): List<Attraction>
    suspend fun isFavourite(attractionId: String, userId: Int): Boolean
}


