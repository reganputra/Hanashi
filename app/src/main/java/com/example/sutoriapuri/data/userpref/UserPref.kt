package com.example.sutoriapuri.data.userpref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.sutoriapuri.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        Log.d(TAG, "Saving session - Token before save: ${user.tokenKey}")
        dataStore.edit {
            it[USER_ID] = user.userId
            it[NAME] = user.name
            it[TOKEN] = user.tokenKey
        }
        dataStore.data.map { preferences ->
            preferences[TOKEN]
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map {
            UserModel(
                it[USER_ID] ?: "",
                it[NAME] ?: "",
                it[TOKEN] ?: ""
            )
        }
    }



    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private const val TAG = "UserPreference"
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_ID = stringPreferencesKey("userId")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")


        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}