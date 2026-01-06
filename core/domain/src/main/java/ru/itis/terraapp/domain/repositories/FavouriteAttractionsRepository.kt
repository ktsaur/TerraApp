package ru.itis.terraapp.domain.repositories

import ru.itis.terraapp.domain.model.Attraction

interface FavouriteAttractionsRepository {
    suspend fun addToFavourites(attraction: Attraction)
    suspend fun removeFromFavourites(attractionId: String)
    suspend fun getFavourites(): List<Attraction>
    suspend fun isFavourite(attractionId: String): Boolean
}


