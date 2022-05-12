package com.fadlan.storyapp.ui.newstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.fadlan.storyapp.R
import com.fadlan.storyapp.data.local.UserDataViewModel
import com.fadlan.storyapp.databinding.ActivityNewStoryBinding
import com.fadlan.storyapp.helper.createTempFile
import com.fadlan.storyapp.helper.reduceFileImage
import com.fadlan.storyapp.helper.uriToFile
import com.fadlan.storyapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class NewStoryActivity : AppCompatActivity() {
    private val newStoryViewModel by viewModels<NewStoryViewModel>()
    private val dataStoreViewModel by viewModels<UserDataViewModel>()
    private lateinit var binding: ActivityNewStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                customToast(getString(R.string.no_permission))
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.new_story_bar)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            uploadStory()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.fadlan.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath).also { getFile = it }
            val os: OutputStream

            // Rotate image to correct orientation
            val bitmap = BitmapFactory.decodeFile(getFile?.path)
            val exif = ExifInterface(currentPhotoPath)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap: Bitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }

            // Convert rotated image to file
            try {
                os = FileOutputStream(myFile)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
                os.flush()
                os.close()

                getFile = myFile
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.previewImageView.setImageBitmap(rotatedBitmap)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.previewImageView.setImageURI(selectedImg)
        }
    }


    private fun uploadStory() {
        setLoadingState(true)

        var isValid = true

        if (getFile == null) {
            Toast.makeText(
                applicationContext,
                getString(R.string.select_an_image),
                LENGTH_SHORT
            ).show()
            isValid = false
        }

        if (binding.textInputCaption.text.toString().isBlank()) {
            binding.outlinedTextField.error = getString(R.string.fill_caption)
            isValid = false
        }

        if (isValid) {

            val file = reduceFileImage(getFile as File)
            val captionText = binding.textInputCaption.text.toString()

            dataStoreViewModel.getSession().observe(this@NewStoryActivity) {
                newStoryViewModel.addNewStory("Bearer ${it.token}", file, captionText)
                newStoryViewModel.message.observe(this@NewStoryActivity) {
                    customToast(getString(R.string.story_uploaded))
                }
            }
        } else {
            setLoadingState(false)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            cameraButton.isEnabled = !isLoading
            galleryButton.isEnabled = !isLoading
            textInputCaption.isEnabled = !isLoading

            loadingBar.visibility = View.VISIBLE
        }
    }

    private fun customToast(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            LENGTH_SHORT
        ).show()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}