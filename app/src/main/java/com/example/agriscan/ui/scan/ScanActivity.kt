package com.example.agriscan.ui.scan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.agriscan.R
import com.example.agriscan.Utils
import com.example.agriscan.Utils.reduceFileImage
import com.example.agriscan.Utils.uriToFile
import com.example.agriscan.databinding.ActivityScanBinding
import com.example.agriscan.network.ApiConfig
import com.example.agriscan.network.ImageUploadResponse
import com.example.agriscan.room.AppDatabase
import com.example.agriscan.room.History
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Locale

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plantName = intent.getStringExtra("PLANT_NAME")
        binding.tvPredictionTitle.text = getString(R.string.prediksi, plantName)

        setupListeners()
        playAnimation()
    }

    private fun setupListeners() {
        binding.btnGallery.setOnClickListener { chooseImageFromGallery() }
        binding.btnSaveHistory.setOnClickListener { saveToHistory() }
        binding.btnCamera.setOnClickListener { checkCameraPermissionAndLaunch() }
    }

    private fun chooseImageFromGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun chooseImageFromCamera() {
        currentImageUri = Utils.getImageUri(this)
        currentImageUri?.let { uri ->
            cameraLauncher.launch(uri)
        } ?: run {
            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToHistory() {
        currentImageUri?.let { uri ->
            val filePath = uriToFile(uri, this).absolutePath
            val condition = binding.tvPredictionResult.text.toString()
            val confidence = binding.tvPredictionConfidence.text.toString()
            val media = when {
                uri.toString().startsWith("content://media/external") -> "Kamera"
                else -> "Galeri"
            }
            val dateTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())
            val plant = intent.getStringExtra("PLANT_NAME") ?: "Unknown"

            val history = History(
                dateTime = dateTime,
                plant = plant,
                condition = condition,
                confidence = confidence,
                media = media,
                imagePath = filePath
            )

            val db = AppDatabase.getInstance(this)

            lifecycleScope.launch {
                db.historyDao().insert(history)
                Toast.makeText(this@ScanActivity, "History tersimpan", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Lakukan prediksi terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }



    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showSelectedImage()
        } else {
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            showSelectedImage()
        } else {
            currentImageUri = null
            Toast.makeText(this, "Pengambilan gambar dibatalkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            chooseImageFromCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImageFromCamera()
            } else {
                Toast.makeText(this, "Permission kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSelectedImage() {
        currentImageUri?.let {
            binding.ivPrediction.setImageURI(it)
            val imageFile = uriToFile(it, this)
            if (imageFile.exists() && imageFile.length() > 0) {
                uploadImageToServer(imageFile)
            } else {
                Toast.makeText(this, "Gambar tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToServer(imageFile: File) {
        val reducedFile = imageFile.reduceFileImage()
        val requestFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", reducedFile.name, requestFile)

        showLoading(true)

        ApiConfig.getApiService().uploadImage(body).enqueue(object : Callback<ImageUploadResponse> {
            override fun onResponse(call: Call<ImageUploadResponse>, response: Response<ImageUploadResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val result = response.body()?.top_predictions?.firstOrNull()
                    if (result != null) {
                        binding.tvPredictionResult.text = result.label
                        binding.tvPredictionConfidence.text = String.format(
                            Locale.US, "%.2f%%", result.confidence * 100
                        )
                        Toast.makeText(this@ScanActivity, "Upload Berhasil!", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvPredictionResult.text = getString(R.string.no_result)
                        binding.tvPredictionConfidence.text = ""
                        Toast.makeText(this@ScanActivity, "Tidak ada prediksi yang ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ScanActivity, "Upload gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                showLoading(false)

                Toast.makeText(this@ScanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnGallery.isEnabled = !isLoading
        binding.btnSaveHistory.isEnabled = !isLoading
        binding.btnCamera.isEnabled = !isLoading
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvPredictionTitle, View.ALPHA, 1f).setDuration(200)
        val image = ObjectAnimator.ofFloat(binding.ivPrediction, View.ALPHA, 1f).setDuration(200)
        val result = ObjectAnimator.ofFloat(binding.tvPredictionResult, View.ALPHA, 1f).setDuration(200)
        val confidence = ObjectAnimator.ofFloat(binding.tvPredictionConfidence, View.ALPHA, 1f).setDuration(200)
        val gallery = ObjectAnimator.ofFloat(binding.btnGallery, View.ALPHA, 1f).setDuration(200)
        val camera = ObjectAnimator.ofFloat(binding.btnCamera, View.ALPHA, 1f).setDuration(200)
        val save = ObjectAnimator.ofFloat(binding.btnSaveHistory, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, image, result, confidence, camera, gallery, save)
            start()
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 101
    }
}
