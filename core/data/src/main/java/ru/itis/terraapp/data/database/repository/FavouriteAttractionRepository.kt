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

    suspend fun removeFromFavourites(attractionId: String) {
        favouriteAttractionDao.deleteFavouriteById(attractionId)
    }

    suspend fun getFavourites(): List<FavouriteAttractionEntity> {
        return favouriteAttractionDao.getAllFavourites()
    }

    suspend fun isFavourite(attractionId: String): Boolean {
        return favouriteAttractionDao.isFavourite(attractionId)
    }
}


