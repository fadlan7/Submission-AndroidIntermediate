package com.fadlan.storyapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fadlan.storyapp.data.local.UserPreference
import com.fadlan.storyapp.data.remote.api.ApiService
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StoriesPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val preference: UserPreference
) : PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val authToken: String = preference.getUser().first().token
            val responseData =
                apiService.getAllStories("Bearer $authToken", position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStoryItem,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStoryItem.isNullOrEmpty()) null else position + 1
            )

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}