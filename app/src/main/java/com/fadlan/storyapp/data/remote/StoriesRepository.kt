package com.fadlan.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.fadlan.storyapp.data.local.StoriesDatabase
import com.fadlan.storyapp.data.local.UserPreference
import com.fadlan.storyapp.data.remote.api.ApiService
import com.fadlan.storyapp.data.remote.response.*
import com.fadlan.storyapp.helper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class StoriesRepository @Inject constructor(
    private val storiesDatabase: StoriesDatabase,
    private val apiService: ApiService,
    private val preference: UserPreference
) {
    private val _login = MutableLiveData<LoginResult>()
    val login: LiveData<LoginResult> = _login

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val listStoryItem: LiveData<List<ListStoryItem>> = _story

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        apiService.userLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _message.value = response.body()?.message
                        _login.value = response.body()?.loginResult
                    }

                    if (!response.isSuccessful) {
                        _message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _message.value = t.message
                    _isLoading.value = false
                }

            })
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        apiService.userRegister(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _message.value = response.body()?.message
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _message.value = t.message
                    _isLoading.value = false
                }
            })
    }

    fun uploadStory(authToken: String, image: File, description: String) {
        val imageReduce = reduceFileImage(image)

        val captionText = description.toRequestBody("text/plain".toMediaType())
        val imageFile = imageReduce.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imageReduce.name,
            imageFile
        )
        val service = apiService.uploadImage(authToken, imageMultipart, captionText)
        service.enqueue(object : Callback<AddNewStoryResponse> {
            override fun onResponse(
                call: Call<AddNewStoryResponse>,
                response: Response<AddNewStoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null && !response.body()!!.error) {
                        _message.value = response.body()!!.message
                    } else {
                        _message.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                _message.value = "Instance failed"
            }

        })
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService, preference),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStory()
            }
        ).liveData
    }

    fun getAllStoriesLocation(authToken: String) {
        apiService.getAllStoriesWithLocation(authToken, 1)
            .enqueue(object : Callback<GetAllStoryResponse> {
                override fun onResponse(
                    call: Call<GetAllStoryResponse>,
                    response: Response<GetAllStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        _story.postValue(response.body()?.listStoryItem)
                    }
                }

                override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                    _message.value = t.message
                }

            })
    }
}