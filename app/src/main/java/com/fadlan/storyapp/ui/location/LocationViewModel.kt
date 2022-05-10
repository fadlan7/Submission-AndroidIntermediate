package com.fadlan.storyapp.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fadlan.storyapp.data.remote.StoriesRepository
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val storyRepository: StoriesRepository) : ViewModel() {
    val storyList: LiveData<List<ListStoryItem>> = storyRepository.story

    fun getAllStoriesLocation(authToken: String) {
        storyRepository.getAllStoriesLocation("Bearer $authToken")
    }
}