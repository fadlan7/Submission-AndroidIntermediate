package com.fadlan.storyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.fadlan.storyapp.data.remote.response.ListStoryItem

class StoryDiffCallback(private val mOldStoryList: List<ListStoryItem>, private val mNewStoryList: List<ListStoryItem>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldStoryList.size
    }

    override fun getNewListSize(): Int {
        return mNewStoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].name == mNewStoryList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldStoryList[oldItemPosition]
        val newEmployee = mNewStoryList[newItemPosition]
        return oldEmployee.name == newEmployee.name && oldEmployee.photoUrl == newEmployee.photoUrl
    }
}