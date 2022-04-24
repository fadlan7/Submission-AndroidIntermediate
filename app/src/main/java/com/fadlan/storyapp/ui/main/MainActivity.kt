package com.fadlan.storyapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadlan.storyapp.R
import com.fadlan.storyapp.ViewModelFactory
import com.fadlan.storyapp.adapter.StoryAdapter
import com.fadlan.storyapp.databinding.ActivityMainBinding
import com.fadlan.storyapp.model.UserPreference
import com.fadlan.storyapp.ui.home.HomeFragment
import com.fadlan.storyapp.ui.newstory.NewStoryFragment
import com.fadlan.storyapp.ui.setting.SettingFragment
import com.fadlan.storyapp.ui.welcome.WelcomeActivity

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var pref: UserPreference
//    private val homeFragment = HomeFragment()
//    private val newStoryFragment = NewStoryFragment()
//    private val settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment)
//            .commit()
//
//        binding.bottomNavigation.setOnNavigationItemReselectedListener { item ->
//            when (item.itemId) {
//                R.id.home -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, homeFragment).commit()
//                }
//                R.id.new_story -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, newStoryFragment).commit()
//                }
//                R.id.setting -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, settingFragment).commit()
//                }
//            }
//        }


        setupViewModel()
        validation()
        showRecyclerList()
//        setupAction()
//        playAnimation()
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
        binding.rvStory.apply {
            layoutManager =
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
        }
    }


    private fun setupViewModel() {
//        mainViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[MainViewModel::class.java]
//
//        mainViewModel.getUser().observe(this) { user ->
//            if (!user.isLogin) {
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
//            }
//        }

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

//        if (!pref.getUser().toString()) {
//
//        }

        mainViewModel.getAllStory(pref.getUser().token)

        mainViewModel.story.observe(this) { storyItem ->
            if (storyItem != null) {
                adapter.initUserData(storyItem)
            }
        }

        mainViewModel.isLoading.observe(this) {
            binding.loadingBar.visibility = View.VISIBLE
        }
    }

    private fun validation() {
        if (!pref.getUser().isLogin) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
//
//    private fun setupAction() {
//        binding.logoutButton.setOnClickListener {
//            mainViewModel.logout()
//        }
//    }
//
//    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()
//
//        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
//        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
//        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(500)
//
//        AnimatorSet().apply {
//            playSequentially(name, message, logout)
//            startDelay = 500
//        }.start()
//    }
}