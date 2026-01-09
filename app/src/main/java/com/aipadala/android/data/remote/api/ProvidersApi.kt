package com.aipadala.android.data.remote.api

import com.aipadala.android.data.remote.dto.ProviderDetailResponse
import com.aipadala.android.data.remote.dto.ProviderListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProvidersApi {

    @GET("v1/providers")
    suspend fun getProviders(
        @Query("corridor") corridor: String? = null
    ): ProviderListResponse

    @GET("v1/providers/{id}")
    suspend fun getProviderDetail(
        @Path("id") providerId: String
    ): ProviderDetailResponse

    @GET("v1/providers/featured")
    suspend fun getFeaturedProviders(): ProviderListResponse
}
