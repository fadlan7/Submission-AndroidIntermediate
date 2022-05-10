package com.fadlan.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fadlan.storyapp.R
import com.fadlan.storyapp.databinding.ActivitySignupBinding
import com.fadlan.storyapp.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {

        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        signupViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        signupViewModel.isLoading.observe(this) {
            binding.loadingBar.visibility = View.VISIBLE
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.input_name),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loadingBar.visibility = View.GONE
                }
                email.isEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.input_email),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loadingBar.visibility = View.GONE
                }
                password.isEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.input_password),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loadingBar.visibility = View.GONE
                }
                else -> {
                    signupViewModel.signUpUser(name, email, password)

                    Toast.makeText(
                        applicationContext, getString(R.string.signup_success),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }
            }
        }

        binding.loginNow.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imgSignup, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appLabel = ObjectAnimator.ofFloat(binding.AppLabel, View.ALPHA, 1f).setDuration(500)
        val imgSignup = ObjectAnimator.ofFloat(binding.imgSignup, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.signupHeader, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val haveAccount =
            ObjectAnimator.ofFloat(binding.haveAccount, View.ALPHA, 1f).setDuration(500)
        val loginNow = ObjectAnimator.ofFloat(binding.loginNow, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                appLabel,
                imgSignup,
                title,
                nameEditTextLayout,
                emailEditTextLayout,
                passwordEditTextLayout,
                signup,
                haveAccount,
                loginNow
            )
            startDelay = 500
        }.start()
    }
}