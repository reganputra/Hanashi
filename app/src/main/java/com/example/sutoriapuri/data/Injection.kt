package com.example.sutoriapuri.data

import android.content.Context
import android.util.Log
import com.example.sutoriapuri.data.api.ApiConfig
import com.example.sutoriapuri.data.userpref.UserPreference
import com.example.sutoriapuri.data.userpref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        Log.d("Injection", "Retrieved token: ${user.token}")
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}