package com.fadlan.storyapp.ui.newstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fadlan.storyapp.data.remote.StoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewStoryViewModel @Inject constructor(private val storyRepository: StoriesRepository) : ViewModel() {
    val message: LiveData<String> = storyRepository.message

    fun addNewStory(authToken: String, imageMultipart: File, caption: String) {
        storyRepository.uploadStory(authToken, imageMultipart, caption)
    }
}