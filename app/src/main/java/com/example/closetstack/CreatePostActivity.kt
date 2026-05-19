package com.example.closetstack

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class CreatePostActivity : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private lateinit var llPlaceholder: LinearLayout
    private lateinit var etDescription: EditText
    private lateinit var btnSave: MaterialButton

    private var selectedUri: Uri? = null
    private var pendingCameraUri: Uri? = null

    // Camera — TakePicture writes a full-res image to a URI we provide
    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            showPhoto(pendingCameraUri!!)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchCamera()
        else Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    // Gallery — modern photo picker, no permission needed
    private val pickPhotoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Persist read access across activity restarts
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) { /* ignore — photo picker URIs usually persist */ }
            showPhoto(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        ivPreview = findViewById(R.id.ivPostPreview)
        llPlaceholder = findViewById(R.id.llPhotoPlaceholder)
        etDescription = findViewById(R.id.etPostDescription)
        btnSave = findViewById(R.id.btnSavePost)

        findViewById<View>(R.id.ivBackCreatePost).setOnClickListener { closeWithSlide() }

        findViewById<View>(R.id.btnTakePhoto).setOnClickListener {
            val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
            if (granted) launchCamera() else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        findViewById<View>(R.id.btnPickGallery).setOnClickListener {
            pickPhotoLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        btnSave.setOnClickListener { savePost() }

        val tvCharCount = findViewById<android.widget.TextView>(R.id.tvCharCount)
        etDescription.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCharCount.text = "${s?.length ?: 0} / 80"
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun launchCamera() {
        // Create a MediaStore entry the camera app can write to.
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "closetstack_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ClosetStack")
            }
        }
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            pendingCameraUri = uri
            takePhotoLauncher.launch(uri)
        } else {
            Toast.makeText(this, "Couldn't open camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPhoto(uri: Uri) {
        selectedUri = uri
        llPlaceholder.visibility = View.GONE
        ivPreview.visibility = View.VISIBLE
        findViewById<View>(R.id.tvReplaceBadge).visibility = View.VISIBLE
        ivPreview.setImageURI(uri)
    }

    private fun savePost() {
        val uri = selectedUri
        if (uri == null) {
            Toast.makeText(this, "Add a photo first", Toast.LENGTH_SHORT).show()
            return
        }
        val description = etDescription.text?.toString()?.trim().orEmpty()

        val post = Post(
            username = "you",
            description = description.ifBlank { "New fit" },
            caption = "Average Rating: 0.0",
            imageRes = 0,
            imageUri = uri.toString(),
            avatarRes = R.drawable.usertop1,
            timestamp = "just now",
            feedType = "all"
        )
        PostRepository.addPost(post)

        Toast.makeText(this, "Post saved!", Toast.LENGTH_SHORT).show()
        closeWithSlide()
    }

    private fun closeWithSlide() {
        finish()
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_left)
    }
}