package com.example.sutoriapuri.data

import android.content.Context
import com.example.sutoriapuri.data.api.ApiConfig
import com.example.sutoriapuri.data.userpref.UserPreference
import com.example.sutoriapuri.data.userpref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.tokenKey)
        return StoryRepository.getInstance(apiService, pref)
    }
}