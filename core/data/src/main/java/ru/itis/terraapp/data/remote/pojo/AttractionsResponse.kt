package ru.itis.terraapp.data.remote.pojo

import com.google.gson.annotations.SerializedName

class AttractionsResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("attractions")
    val attractions: List<AttractionItem>?,
    @SerializedName("total")
    val total: Int?
)

class AttractionItem(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)

