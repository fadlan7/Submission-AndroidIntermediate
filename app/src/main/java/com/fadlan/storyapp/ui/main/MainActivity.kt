package com.fadlan.storyapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fadlan.storyapp.R
import com.fadlan.storyapp.adapter.LoadingStateAdapter
import com.fadlan.storyapp.adapter.StoryAdapter
import com.fadlan.storyapp.data.local.UserDataViewModel
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.databinding.ActivityMainBinding
import com.fadlan.storyapp.ui.location.LocationActivity
import com.fadlan.storyapp.ui.newstory.NewStoryActivity
import com.fadlan.storyapp.ui.welcome.WelcomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private val dataStoreViewModel by viewModels<UserDataViewModel>()
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var storyListAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        showRecyclerList()

        binding.floatingActionButton.setOnClickListener {
            Intent(this, NewStoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_btn -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.title_logout))
                    .setMessage(resources.getString(R.string.supporting_text_logout))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        dataStoreViewModel.logout()
                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finish()
                    }
                    .show()
                true
            }
            R.id.language_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.maps_btn -> {
                startActivity(Intent(this, LocationActivity::class.java))
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    private fun showRecyclerList() {
        storyListAdapter = StoryAdapter()

        recyclerView = binding.rvStory

        recyclerView.apply {
            adapter = storyListAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyListAdapter.retry()
                }
            )

            layoutManager =
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
        }
    }


    private fun setupViewModel() {
        dataStoreViewModel.getSession().observe(this) { UserModel ->
            if (!UserModel.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        mainViewModel.listStory.observe(this@MainActivity) {
                            updateRecyclerViewData(it)
                        }
                    }
                }
            }
        }

        mainViewModel.listStory.observe(this) {
            storyListAdapter.submitData(lifecycle, it)
        }

        mainViewModel.isLoading.observe(this) { loading(it) }
    }

    private fun loading(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateRecyclerViewData(stories: PagingData<ListStoryItem>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        storyListAdapter.submitData(lifecycle, stories)
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}