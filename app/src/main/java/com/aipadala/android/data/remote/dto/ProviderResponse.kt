package com.aipadala.android.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProviderListResponse(
    @Json(name = "providers") val providers: List<ProviderDto>
)

@JsonClass(generateAdapter = true)
data class ProviderDetailResponse(
    @Json(name = "provider") val provider: ProviderDto,
    @Json(name = "reviews") val reviews: List<ReviewDto>? = null,
    @Json(name = "corridors") val corridors: List<CorridorDto>? = null
)

@JsonClass(generateAdapter = true)
data class ProviderDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "displayName") val displayName: String,
    @Json(name = "logoUrl") val logoUrl: String?,
    @Json(name = "trustScore") val trustScore: Int,
    @Json(name = "rating") val rating: Double?,
    @Json(name = "reviewCount") val reviewCount: Int?,
    @Json(name = "payoutMethods") val payoutMethods: List<String>,
    @Json(name = "avgDeliveryTime") val avgDeliveryTime: String?,
    @Json(name = "description") val description: String?
)

@JsonClass(generateAdapter = true)
data class ReviewDto(
    @Json(name = "id") val id: String,
    @Json(name = "author") val author: String,
    @Json(name = "rating") val rating: Int,
    @Json(name = "content") val content: String,
    @Json(name = "date") val date: String
)

@JsonClass(generateAdapter = true)
data class CorridorDto(
    @Json(name = "from") val from: String,
    @Json(name = "to") val to: String,
    @Json(name = "isSupported") val isSupported: Boolean
)
