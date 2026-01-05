package ru.itis.terraapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.itis.terraapp.data.remote.pojo.AttractionsResponse

interface AttractionsApi {
    @GET("attractions")
    suspend fun getAttractionsByCityName(
        @Query("city") cityName: String
    ): AttractionsResponse?
}

