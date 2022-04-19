package com.fadlan.storyapp.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fadlan.storyapp.R
import com.fadlan.storyapp.ViewModelFactory
import com.fadlan.storyapp.databinding.ActivityMainBinding
import com.fadlan.storyapp.model.UserPreference
import com.fadlan.storyapp.ui.home.HomeFragment
import com.fadlan.storyapp.ui.newstory.NewStoryFragment
import com.fadlan.storyapp.ui.setting.SettingFragment
import com.fadlan.storyapp.ui.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.concurrent.fixedRateTimer

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val newStoryFragment = NewStoryFragment()
    private val settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        bottomNavigation = binding.bottomNavigation

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment)
            .commit()


        binding.bottomNavigation.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment).commit()
                }
                R.id.new_story -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, newStoryFragment).commit()
                }
                R.id.setting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, settingFragment).commit()
                }
            }
        }


//        setupViewModel()
//        setupAction()
//        playAnimation()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                Toast.makeText(
                    applicationContext, getString(R.string.greeting, user.name),
                    Toast.LENGTH_SHORT
                )
                    .show()
//                binding.nameTextView.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
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