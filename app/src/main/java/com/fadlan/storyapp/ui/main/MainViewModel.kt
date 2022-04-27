package com.fadlan.storyapp.ui.main

import androidx.lifecycle.*
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.GetAllStoryResponse
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _story = MutableLiveData<ArrayList<ListStoryItem>>()
    val story: LiveData<ArrayList<ListStoryItem>> = _story

    private val _message = MutableLiveData<String>()
    val message:LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStory(token: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getAllStories("Bearer $token")
            .enqueue(object : Callback<GetAllStoryResponse> {
                override fun onResponse(
                    call: Call<GetAllStoryResponse>,
                    response: Response<GetAllStoryResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _story.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                    _message.value = t.message
                    _isLoading.value = false
                }

            })
    }
}