package com.fadlan.storyapp.data.remote.api

import com.fadlan.storyapp.data.remote.response.AddNewStoryResponse
import com.fadlan.storyapp.data.remote.response.GetAllStoryResponse
import com.fadlan.storyapp.data.remote.response.LoginResponse
import com.fadlan.storyapp.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetAllStoryResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddNewStoryResponse>

    @GET("stories")
    fun getAllStoriesWithLocation(
        @Header("Authorization") auth: String,
        @Query("location")location: Int
    ): Call<GetAllStoryResponse>
}