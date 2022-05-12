package com.fadlan.storyapp.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val preference: UserPreference):ViewModel() {
    fun getSession():LiveData<UserModel>{
        return preference.getUser().asLiveData()
    }

    fun saveSession(userModel: UserModel){
        viewModelScope.launch {
            preference.saveUser(userModel)
        }
    }

    fun logout(){
        viewModelScope.launch {
            preference.logout()
        }
    }
}