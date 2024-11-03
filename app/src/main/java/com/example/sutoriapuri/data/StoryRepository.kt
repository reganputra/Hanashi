package com.example.sutoriapuri.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.sutoriapuri.data.api.ApiService
import com.example.sutoriapuri.data.model.UserModel
import com.example.sutoriapuri.data.response.ErrorResponse
import com.example.sutoriapuri.data.response.LoginResponse
import com.example.sutoriapuri.data.response.RegisterResponse
import com.example.sutoriapuri.data.userpref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
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
            if (response.error == false){
                response.loginResult?.let { loginResult ->
                    val user = UserModel(
                        userId = loginResult.userId ?: "",
                        name = loginResult.name ?: "",
                        token = loginResult.token ?: ""
                    )
                    userPref.saveSession(user)
                }

                emit(Result.Success(response))
            }

        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }


    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }

    suspend fun logout() {
        userPref.logout()
    }

    companion object {
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