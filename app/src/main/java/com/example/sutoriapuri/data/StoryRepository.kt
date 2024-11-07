package com.example.sutoriapuri.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.sutoriapuri.data.api.ApiService
import com.example.sutoriapuri.data.model.UserModel
import com.example.sutoriapuri.data.response.ErrorResponse
import com.example.sutoriapuri.data.response.ListStoryItem
import com.example.sutoriapuri.data.response.LoginResponse
import com.example.sutoriapuri.data.response.RegisterResponse
import com.example.sutoriapuri.data.userpref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class StoryRepository(
    private val apiService: ApiService,
    private val userPref: UserPreference
) {

    fun userRegister(
        name: String,
        email: String,
        password: String): LiveData<Result<RegisterResponse>> = liveData {
            emit(Result.Loading)
        try {
            val response = apiService.register(name, email , password)
            emit(Result.Success(response))
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            Log.d(TAG, "Login Response: $response")
            if (response.error == false){
                response.loginResult?.let { loginResult ->
                    Log.d(TAG, "Token received from login: ${loginResult.token}")
                    val user = UserModel(
                        userId = loginResult.userId ?: "",
                        name = loginResult.name ?: "",
                        token = loginResult.token ?: ""

                    )
                    Log.d(TAG, "Saving user session with token: ${user.token}")
                    userPref.saveSession(user)
                }
                emit(Result.Success(response))
            }

        }catch (e: HttpException){
            Log.e(TAG, "Login error", e)
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

//    fun getAllStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            Log.d(TAG, "Getting stories...")
//            val response = apiService.getStories()
//            val stories = response.listStory
//            Log.d(TAG, "Successfully retrieved ${stories.size} stories")
//            emit(Result.Success(stories))
//
//        }catch (e: HttpException){
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//            val errorMessage = errorBody.message
//            emit(Result.Error(errorMessage.toString()))
//        }
//    }

    fun getAllStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            // Ambil token dari UserPreference
            val user = userPref.getSession().first()
            val token = user.token
            if (token.isNotEmpty()) {
                // Kirim permintaan API dengan token
                val response = apiService.getStories("Bearer $token")
                val stories = response.listStory
                Log.d(TAG, "Successfully retrieved ${stories.size} stories")
                emit(Result.Success(stories))
            } else {
                emit(Result.Error("Token is missing"))
            }

        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun getStoryById(id: String): LiveData<Result<ListStoryItem>> = liveData {
        emit(Result.Loading)
        try {
            val user = userPref.getSession().first()
            val token = user.token
            if (token.isNotEmpty()) {
                val response = apiService.getStoriesById("Bearer $token", id)
//                val story = response.story?: ListStoryItem(
//                    photoUrl = "",
//                    createdAt = "",
//                    name = "",
//                    description = "",
//                    lon = 0.0,
//                    id = "",
//                    lat = 0.0
//
//                )
                response.story?.let { story ->
                    emit(Result.Success(story))
                }

            }
        } catch (e: HttpException){
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }


    fun getSession(): Flow<UserModel> {
        Log.d(TAG, "Getting user session")
        return userPref.getSession()
    }

    suspend fun logout() {
        Log.d(TAG, "Logging out user")
        userPref.logout()
    }

    companion object {
        private const val TAG = "StoryRepository"
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}