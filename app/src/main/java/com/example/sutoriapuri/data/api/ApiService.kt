package com.example.sutoriapuri.data.api

import com.example.sutoriapuri.data.response.ListStoryResponse
import com.example.sutoriapuri.data.response.LoginResponse
import com.example.sutoriapuri.data.response.RegisterResponse
import com.example.sutoriapuri.data.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ListStoryResponse

    @GET("stories/{id}")
    suspend fun getStoriesById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ListStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): UploadStoryResponse

//    @GET("stories")
//    suspend fun getStoriesWithLocation(
//        @Header("Authorization")token: String,
//        @Query("location") location : Int = 1,
//    ) : ListStoryResponse

}