package com.fadlan.storyapp.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.RegisterResponse
import com.fadlan.storyapp.model.UserModel
import com.fadlan.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
//    fun saveUser(user: UserModel) {
//        viewModelScope.launch {
//            pref.saveUser(user)
//        }
//    }

//    private lateinit var mUser:UserPreference
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun signUpUser(name: String, email: String, password: String) {
        isLoading.value = true

        ApiConfig.getApiService().userRegister(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        message.value = response.body()?.message
                     }
                    if (!response.isSuccessful) {
                        message.value = response.message()
                    }

                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    message.value = t.message
                    isLoading.value = false
                }
            })
    }
//    fun saveUser(user: UserModel) {
//        viewModelScope.launch {
//            mUser.saveUser(user)
//        }
//    }
}