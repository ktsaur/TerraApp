package ru.itis.terraapp.data.remote

import retrofit2.http.GET

interface OpenTripMapApi {

    @GET
    suspend fun getGeographicCoordinates(): Any
}