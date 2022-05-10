package com.fadlan.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signUpUser(name: String, email: String, password: String) {
        _isLoading.value = true

        ApiConfig.getApiService().userRegister(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _message.value = response.body()?.message
                    }
                    if (!response.isSuccessful) {
                        _message.value = response.message()
                    }

                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _message.value = t.message
                    _isLoading.value = false
                }
            })
    }
}