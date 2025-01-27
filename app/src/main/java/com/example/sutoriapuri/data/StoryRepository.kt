package com.example.sutoriapuri.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.sutoriapuri.data.api.ApiService
import com.example.sutoriapuri.data.model.UserModel
import com.example.sutoriapuri.data.response.ErrorResponse
import com.example.sutoriapuri.data.response.ListStoryItem
import com.example.sutoriapuri.data.response.LoginResponse
import com.example.sutoriapuri.data.response.RegisterResponse
import com.example.sutoriapuri.data.response.UploadStoryResponse
import com.example.sutoriapuri.data.userpref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
                        tokenKey = loginResult.token ?: ""

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

//    fun getAllStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val user = userPref.getSession().first()
//            val token = user.tokenKey
//            if (token.isNotEmpty()) {
//                val response = apiService.getStories("Bearer $token")
//                val stories = response.listStory
//                emit(Result.Success(stories))
//            } else {
//                emit(Result.Error("Token is missing"))
//            }
//
//        } catch (e: HttpException) {
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//            val errorMessage = errorBody.message
//            emit(Result.Error(errorMessage.toString()))
//        }
//    }

    fun getStoryById(id: String): LiveData<Result<ListStoryItem>> = liveData {
        emit(Result.Loading)
        try {
            val user = userPref.getSession().first()
            val token = user.tokenKey
            if (token.isNotEmpty()) {
                val response = apiService.getStoriesById("Bearer $token", id)
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

    fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody): LiveData<Result<UploadStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val user = userPref.getSession().first()
            val token = user.tokenKey
            if (token.isNotEmpty()){
                val response = apiService.uploadStory(file, description, "Bearer $token")
                emit(Result.Success(response))
            }
        } catch (e: HttpException){
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }

    }

    fun storiesPaging(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPref)
            }
        ).liveData
    }

    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val user = userPref.getSession().first()
            val token = user.tokenKey
            if (token.isNotEmpty()) {
                val response = apiService.getStoriesWithLocation("Bearer $token", location = 1)
                val stories = response.listStory
                emit(Result.Success(stories))
            }
        } catch (e: HttpException){
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