package com.fadlan.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fadlan.storyapp.databinding.ActivityDetailBinding
import com.fadlan.storyapp.databinding.ActivityLoginBinding
import com.fadlan.storyapp.helper.Constanta.EXTRA_CAPTION
import com.fadlan.storyapp.helper.Constanta.EXTRA_IMAGE
import com.fadlan.storyapp.helper.Constanta.EXTRA_USER_NAME

class DetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(intent.getStringExtra(EXTRA_IMAGE))
                .into(ivStory)

            tvUserName.text =intent.getStringExtra(EXTRA_USER_NAME)
            tvCaption.text = intent.getStringExtra(EXTRA_CAPTION)
        }
    }
}