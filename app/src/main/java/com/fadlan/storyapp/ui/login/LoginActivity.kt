package com.fadlan.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.ViewModelProvider
import com.fadlan.storyapp.R
import com.fadlan.storyapp.databinding.ActivityLoginBinding
import com.fadlan.storyapp.helper.Constanta.LOGIN_PREF
import com.fadlan.storyapp.ui.main.MainActivity
import com.fadlan.storyapp.data.local.UserModel
import com.fadlan.storyapp.data.local.UserPreference
import com.fadlan.storyapp.ui.signup.SignupActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var user: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)
        user = UserPreference(this)

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
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.message.observe(this) {
            Toast.makeText(this, it, LENGTH_SHORT).show()
        }

        loginViewModel.isLoading.observe(this) {
            binding.loadingBar.visibility = View.VISIBLE
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            when {
                email.isEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.input_email),
                        LENGTH_SHORT
                    ).show()
                    binding.loadingBar.visibility = View.GONE
                }
                password.isEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.input_password),
                        LENGTH_SHORT
                    ).show()
                    binding.loadingBar.visibility = View.GONE
                }
                else -> {
                    loginViewModel.getUserLogin(email, password)
                    loginViewModel.login.observe(this) { loginResult ->
                        binding.loadingBar.visibility = View.VISIBLE

                        if (loginResult != null) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.login_success),
                                LENGTH_SHORT
                            ).show()

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
        user.saveUser(userModel)
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
}