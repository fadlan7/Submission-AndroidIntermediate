package com.fadlan.storyapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadlan.storyapp.data.remote.response.ListStoryItem

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<ListStoryItem>)

    @Query("SELECT * FROM ListStoryItem")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM ListStoryItem")
    suspend fun deleteAll()
}