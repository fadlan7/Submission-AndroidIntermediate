package com.fadlan.storyapp.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import packagecom.fadlan.storyapp.model.UserModel
import packagecom.fadlan.storyapp.model.UserPreference

class SignupViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}