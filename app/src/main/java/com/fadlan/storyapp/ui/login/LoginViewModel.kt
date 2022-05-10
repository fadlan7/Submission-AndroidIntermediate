package com.fadlan.storyapp.ui.login

import androidx.lifecycle.*
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.LoginResponse
import com.fadlan.storyapp.data.remote.response.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _login = MutableLiveData<LoginResult>()
    val login : LiveData<LoginResult> = _login

    private val _message = MutableLiveData<String>()
    val message:LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserLogin(email: String, password: String) {
        _isLoading.value = true

        ApiConfig.getApiService().userLogin(email, password)
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
}