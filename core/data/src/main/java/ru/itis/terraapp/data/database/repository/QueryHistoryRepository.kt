package ru.itis.terraapp.data.database.repository

import ru.itis.terraapp.data.database.dao.QueryHistoryDao
import ru.itis.terraapp.data.database.entity.QueryHistoryEntity
import javax.inject.Inject

class QueryHistoryRepository @Inject constructor(
    private val queryHistoryDao: QueryHistoryDao
) {
    suspend fun saveQuery(queryHistoryEntity: QueryHistoryEntity) {
        return queryHistoryDao.insertQuery(queryHistoryEntity = queryHistoryEntity)
    }

    suspend fun getTwoLastQueryForCity(city: String): Long? {
        return queryHistoryDao.getLastQueryForCity(city = city)
    }

    suspend fun countQueryBetween(start: Long, end: Long): Int {
        return queryHistoryDao.countQueryBetween(start = start, end = end)
    }
}