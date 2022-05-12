package com.fadlan.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fadlan.storyapp.data.remote.StoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val storyRepository: StoriesRepository) : ViewModel() {

    val message: LiveData<String> = storyRepository.message
    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun signUpUser(name: String, email: String, password: String) =
        storyRepository.register(name, email, password)
}