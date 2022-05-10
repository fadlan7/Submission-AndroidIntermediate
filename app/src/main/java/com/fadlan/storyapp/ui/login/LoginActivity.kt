package com.fadlan.storyapp.ui.login

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
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import com.fadlan.storyapp.R
import com.fadlan.storyapp.data.local.DataStoreViewModel
import com.fadlan.storyapp.databinding.ActivityLoginBinding
import com.fadlan.storyapp.ui.main.MainActivity
import com.fadlan.storyapp.data.local.UserModel
import com.fadlan.storyapp.ui.signup.SignupActivity


class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        loginViewModel.message.observe(this) {
            Toast.makeText(this, it, LENGTH_SHORT).show()
        }

        loginViewModel.isLoading.observe(this) {
            loading(it)
        }
    }

    private fun loading(loading: Boolean) {
        binding.loadingBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            when {
                email.isEmpty() -> {
                    customToast(getString(R.string.input_email))
                    binding.loadingBar.visibility = View.GONE
                }
                password.isEmpty() -> {
                    customToast(getString(R.string.input_password))
                    binding.loadingBar.visibility = View.GONE
                }
                else -> {
                    loginViewModel.getUserLogin(email, password)

                    loginViewModel.login.observe(this) { loginResult ->
                        binding.loadingBar.visibility = View.VISIBLE

                        if (loginResult != null) {
                            customToast(getString(R.string.login_success))

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()

                            //save session
                            session(
                                UserModel(
                                    loginResult.token, true
                                )
                            )
                        }
                    }
                }
            }
        }

        binding.signupHere.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun session(userModel: UserModel) {
        dataStoreViewModel.saveSession(userModel)
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appLabel = ObjectAnimator.ofFloat(binding.AppLabel, View.ALPHA, 1f).setDuration(500)
        val imgLogin = ObjectAnimator.ofFloat(binding.imgLogin, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.loginHeader, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val noAccount = ObjectAnimator.ofFloat(binding.noAccount, View.ALPHA, 1f).setDuration(500)
        val signupHere = ObjectAnimator.ofFloat(binding.signupHere, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                appLabel,
                imgLogin,
                title,
                emailEditTextLayout,
                passwordEditTextLayout,
                signup,
                noAccount,
                signupHere
            )
            startDelay = 500
        }.start()
    }

    private fun customToast(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            LENGTH_SHORT
        ).show()
    }
}