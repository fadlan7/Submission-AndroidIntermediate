package com.fadlan.storyapp.ui.main

import androidx.lifecycle.*
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.GetAllStoryResponse
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.model.UserModel
import com.fadlan.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private lateinit var pref: UserPreference
    val story = MutableLiveData<ArrayList<ListStoryItem>>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun getAllStory(token: String) {
        isLoading.value = true

        ApiConfig.getApiService().getAllStories("Bearer $token")
            .enqueue(object : Callback<GetAllStoryResponse> {
                override fun onResponse(
                    call: Call<GetAllStoryResponse>,
                    response: Response<GetAllStoryResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        story.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                    message.value = t.message
                    isLoading.value = false
                }

            })
    }

    //    fun getUser(): LiveData<UserModel> {
//        return pref.getUser().asLiveData()
//    }
//
    fun logout() {
        pref.logout()
    }
}