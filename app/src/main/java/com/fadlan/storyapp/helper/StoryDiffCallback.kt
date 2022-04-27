package com.fadlan.storyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.fadlan.storyapp.data.remote.response.ListStoryItem

class StoryDiffCallback(
    private val mOldStoryList: List<ListStoryItem>,
    private val mNewStoryList: List<ListStoryItem>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldStoryList.size
    }

    override fun getNewListSize(): Int {
        return mNewStoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            mOldStoryList[oldItemPosition].id != mNewStoryList[newItemPosition].id ->{
                false
            }
            mOldStoryList[oldItemPosition].name != mNewStoryList[newItemPosition].name ->{
                false
            }
            mOldStoryList[oldItemPosition].photoUrl != mNewStoryList[newItemPosition].photoUrl ->{
                false
            }
            mOldStoryList[oldItemPosition].createdAt != mNewStoryList[newItemPosition].createdAt ->{
                false
            }
            mOldStoryList[oldItemPosition].description != mNewStoryList[newItemPosition].description ->{
                false
            }
            mOldStoryList[oldItemPosition].lat != mNewStoryList[newItemPosition].lat ->{
                false
            }
            mOldStoryList[oldItemPosition].lon != mNewStoryList[newItemPosition].lon ->{
                false
            }
            else -> true
        }
    }
}