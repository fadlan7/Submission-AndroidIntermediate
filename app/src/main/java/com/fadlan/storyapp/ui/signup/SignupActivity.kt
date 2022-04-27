package com.fadlan.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fadlan.storyapp.R
import com.fadlan.storyapp.databinding.ActivitySignupBinding
import com.fadlan.storyapp.helper.FieldValidators
import com.fadlan.storyapp.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListeners()
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
                    binding.nameEditTextLayout.error = getString(R.string.input_name)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.input_password)
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

    private fun setupListeners() {
        binding.emailEditText.addTextChangedListener(TextFieldValidation(binding.emailEditText))
        binding.passwordEditText.addTextChangedListener(TextFieldValidation(binding.passwordEditText))
    }

    private fun validateEmail(): Boolean {
        if (!FieldValidators.isValidEmail(binding.emailEditText.text.toString())) {
            binding.emailEditTextLayout.error =  getString(R.string.invalid_email)
            binding.emailEditText.requestFocus()
            return false
        } else {
            binding.emailEditTextLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.passwordEditText.text.toString().length < 6) {
            binding.passwordEditTextLayout.error =  getString(R.string.password_cant_be_less)
            binding.passwordEditText.requestFocus()
            return false
        } else {
            binding.passwordEditTextLayout.isErrorEnabled = false
        }
        return true
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.emailEditText -> {
                    validateEmail()
                }
                R.id.passwordEditText -> {
                    validatePassword()
                }
            }
        }
    }
}