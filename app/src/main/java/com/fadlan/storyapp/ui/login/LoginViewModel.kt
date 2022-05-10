package com.fadlan.storyapp.ui.login

import androidx.lifecycle.*
import com.fadlan.storyapp.data.remote.StoriesRepository
import com.fadlan.storyapp.data.remote.response.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val storyRepository: StoriesRepository) : ViewModel() {

    val login: LiveData<LoginResult> = storyRepository.login
    val message: LiveData<String> = storyRepository.message
    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun getUserLogin(email: String, password: String) = storyRepository.login(email, password)
}