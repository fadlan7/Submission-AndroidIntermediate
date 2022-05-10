package com.fadlan.storyapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fadlan.storyapp.data.remote.StoriesRepository
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.data.remote.response.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val storyRepository: StoriesRepository) : ViewModel() {

    val login: LiveData<LoginResult> = storyRepository.login
    val message: LiveData<String> = storyRepository.message
    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    val storyList: LiveData<PagingData<ListStoryItem>> = storyRepository.getAllStories().cachedIn(viewModelScope)
}