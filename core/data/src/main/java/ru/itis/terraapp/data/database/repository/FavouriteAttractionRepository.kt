package ru.itis.terraapp.data.database.repository

import ru.itis.terraapp.data.database.dao.FavouriteAttractionDao
import ru.itis.terraapp.data.database.entity.FavouriteAttractionEntity
import javax.inject.Inject

class FavouriteAttractionRepository @Inject constructor(
    private val favouriteAttractionDao: FavouriteAttractionDao
) {

    suspend fun addToFavourites(entity: FavouriteAttractionEntity) {
        favouriteAttractionDao.insertFavourite(entity)
    }

    suspend fun removeFromFavourites(attractionId: String, userId: Int) {
        favouriteAttractionDao.deleteFavouriteById(attractionId, userId)
    }

    suspend fun getFavourites(userId: Int): List<FavouriteAttractionEntity> {
        return favouriteAttractionDao.getAllFavourites(userId)
    }

    suspend fun isFavourite(attractionId: String, userId: Int): Boolean {
        return favouriteAttractionDao.isFavourite(attractionId, userId)
    }
}


