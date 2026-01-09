package com.aipadala.android.core.di

import com.aipadala.android.BuildConfig
import com.aipadala.android.data.remote.api.ProvidersApi
import com.aipadala.android.data.remote.api.RatesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        // Certificate pinning for production API endpoints
        // To obtain the certificate hash, run:
        // openssl s_client -servername api.aipadala.com -connect api.aipadala.com:443 | \
        //   openssl x509 -pubkey -noout | \
        //   openssl pkey -pubin -outform der | \
        //   openssl dgst -sha256 -binary | openssl enc -base64
        val certificatePinner = CertificatePinner.Builder()
            // Production API certificate pins
            // TODO: Replace with actual certificate hashes before production release
            // Add backup pins to prevent lockout during certificate rotation
            .add("api.aipadala.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=") // Primary pin (placeholder)
            .add("api.aipadala.com", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=") // Backup pin (placeholder)
            // Staging API - only enforce in release builds
            .add("api-staging.aipadala.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-App-Platform", "android")
                    .addHeader("X-App-Version", BuildConfig.VERSION_NAME)
                    .build()
                chain.proceed(request)
            }
            // Only enable certificate pinning in release builds
            // This allows debugging with proxy tools during development
            .apply {
                if (!BuildConfig.DEBUG) {
                    certificatePinner(certificatePinner)
                }
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideRatesApi(retrofit: Retrofit): RatesApi =
        retrofit.create(RatesApi::class.java)

    @Provides
    @Singleton
    fun provideProvidersApi(retrofit: Retrofit): ProvidersApi =
        retrofit.create(ProvidersApi::class.java)
}
