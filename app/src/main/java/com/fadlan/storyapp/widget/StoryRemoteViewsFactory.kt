package com.fadlan.storyapp.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.fadlan.storyapp.R
import com.fadlan.storyapp.data.local.UserPreference
import com.fadlan.storyapp.data.remote.api.ApiConfig
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class StoryRemoteViewsFactory(private val context: Context):RemoteViewsService.RemoteViewsFactory {
    private lateinit var pref: UserPreference
    private val stories = ArrayList<ListStoryItem>()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate() {

    }

    override fun onDataSetChanged() {

        CoroutineScope(Dispatchers.Main).launch {
            mainViewModel.getAllStory(pref.getUser().token)
        }


        try {
            val listStories = ApiConfig.getApiService()
                .getAllStories(pref.getUser().token )
                .execute()
                .body()
                ?.listStory as List<ListStoryItem>
            stories.clear()
            stories.addAll(listStories)
        } catch (e: Exception) {
            Log.e(TAG, "onResponse: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount() = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item).apply {
            val image = Glide.with(context)
                .asBitmap()
                .load(stories[position].photoUrl)
                .submit()
                .get()

            setImageViewBitmap(R.id.imageView, image)
        }

        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to stories[position].name
        )

        val fillIntent = Intent().apply {
            putExtras(extras)
        }

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount() = 1

    override fun getItemId(p0: Int) = 0L

    override fun hasStableIds() = false

    companion object {
        private const val TAG = "StoriesRemoteViewsFacto"
    }
}