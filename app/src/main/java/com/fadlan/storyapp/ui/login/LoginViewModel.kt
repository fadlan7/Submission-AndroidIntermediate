package com.fadlan.storyapp.ui.login

import androidx.lifecycle.*
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.LoginResponse
import com.fadlan.storyapp.data.remote.response.LoginResult
import com.fadlan.storyapp.model.UserModel
import com.fadlan.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private lateinit var pref: UserPreference
    val login = MutableLiveData<LoginResult>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()


    fun getUserLogin(email: String, password: String) {
        isLoading.value = true

        ApiConfig.getApiService().userLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        message.value = response.body()?.message
                        login.value = response.body()?.loginResult
                    }
                    if (!response.isSuccessful) {
                        message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    message.value = t.message
                    isLoading.value = false
                }
            })
    }

//    fun saveUser(user: UserModel) {
//        viewModelScope.launch {
//            pref.saveUser(user)
//        }
//    }
}