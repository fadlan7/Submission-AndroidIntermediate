package com.fadlan.storyapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fadlan.storyapp.databinding.ActivityDetailBinding
import com.fadlan.storyapp.helper.Constanta.EXTRA_CAPTION
import com.fadlan.storyapp.helper.Constanta.EXTRA_IMAGE
import com.fadlan.storyapp.helper.Constanta.EXTRA_UPLOAD_DATE
import com.fadlan.storyapp.helper.Constanta.EXTRA_USER_NAME
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(intent.getStringExtra(EXTRA_IMAGE))
                .into(ivStory)

            val strDateTime = intent.getStringExtra(EXTRA_UPLOAD_DATE)
            val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val date = strDateTime?.let { sdfInput.parse(it) } as Date
            val sdfOutput = DateFormat.getDateInstance(DateFormat.FULL).format(date)
            val formatted: String = sdfOutput.format(date)

            tvUserName.text = intent.getStringExtra(EXTRA_USER_NAME)
            tvUploadDate.text = formatted
            tvCaption.text = intent.getStringExtra(EXTRA_CAPTION)
        }
    }
}