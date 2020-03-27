package com.example.jetpack.repository.remote

import com.example.jetpack.utilities.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceFactory {
    companion object {
        private var INSTANCE: ApiService? = null

        fun getService(): ApiService {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: Retrofit.Builder().baseUrl(BASE_URL).client(provideOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build().create(ApiService::class.java).also { INSTANCE = it }
                }
        }

        private fun provideOkHttpClient(): OkHttpClient {
            val interceptor =
                HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
    }
}