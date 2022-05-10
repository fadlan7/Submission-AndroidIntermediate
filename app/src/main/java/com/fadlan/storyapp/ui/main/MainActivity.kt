package com.fadlan.storyapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fadlan.storyapp.R
import com.fadlan.storyapp.adapter.StoryAdapter
import com.fadlan.storyapp.databinding.ActivityMainBinding
import com.fadlan.storyapp.data.local.UserPreference
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.ui.newstory.NewStoryActivity
import com.fadlan.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    //    private val storyListAdapter by lazy { StoryAdapter() }
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyListAdapter: StoryAdapter
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

        setupViewModel()
        validation()
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
                pref.logout()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                true
            }
            R.id.language_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }

    private fun showRecyclerList() {
        storyListAdapter = StoryAdapter()

        recyclerView = binding.rvStory

        recyclerView.apply {
            adapter = storyListAdapter

            layoutManager =
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
        }
    }


    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainViewModel.story.observe(this@MainActivity){
                    updateRecyclerViewData(it)
                }
                mainViewModel.getAllStory(pref.getUser().token)
            }
        }

        mainViewModel.isLoading.observe(this) { loading(it) }
    }

    private fun loading(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun validation() {
        if (!pref.getUser().isLogin) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun updateRecyclerViewData(stories: List<ListStoryItem>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

        storyListAdapter.submitList(stories)

        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

}