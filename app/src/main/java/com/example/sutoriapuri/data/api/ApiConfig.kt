package com.example.sutoriapuri.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val TAG = "ApiConfig"
        fun getApiService(token: String): ApiService {
            Log.d(TAG, "Creating ApiService with token: $token")
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            val authInterceptor = Interceptor { chain ->
//                val req = chain.request()
//                val requestHeaders = req.newBuilder()
//                    .addHeader("Authorization", "Bearer $token")
//                    .build()
//                Log.d(TAG, "Request URL: ${req.url}")
//                Log.d(TAG, "Request Headers: ${requestHeaders.headers}")
//                Log.d(TAG, "Authorization Header: Bearer $token")
//                chain.proceed(requestHeaders)
//            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
//                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}