package com.example.jetpack.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jetpack.databinding.FragmentCameraBinding
import com.example.jetpack.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel>()

    private val permissions = arrayOf(Manifest.permission.CAMERA)
    private var lengthFace = CameraSelector.LENS_FACING_FRONT
    private fun checkPermissions() = permissions.all {
        ActivityCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    lateinit var binding: FragmentCameraBinding
    lateinit var imageCapture: ImageCapture
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 101)
        }
    }

    private fun startCamera() {
        //CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lengthFace).build()

        //CameraProvider
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val aspectRatio = AspectRatio.RATIO_4_3
            val rotation = binding.viewFinder.display.rotation
            //preview case
            val preview = Preview.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetRotation(rotation)
                .build()
            preview.setSurfaceProvider(binding.viewFinder.previewSurfaceProvider)

            //ImageCapture
            imageCapture =
                ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .setTargetAspectRatio(aspectRatio)
                    .setTargetRotation(rotation)
                    .build()
            binding.captureBtn.setOnClickListener {
                takePicture()
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun takePicture() {
        val dir = requireContext().externalMediaDirs.first()
        val photoFile = File(dir, "${System.currentTimeMillis()}.jpg")
        val metadata = ImageCapture.Metadata().apply {
            isReversedHorizontal = true
        }
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()
        imageCapture.takePicture(
            outputFileOptions,
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            viewModel.avatar.postValue(photoFile)
                            Glide.with(this@CameraFragment)
                                .load(Uri.fromFile(photoFile))
                                .apply(RequestOptions.circleCropTransform())
                                .into(binding.savedPicture)
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    TODO("Not yet implemented")
                }
            })

        //flash when taking picture
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.root.postDelayed({
                binding.root.foreground = ColorDrawable(Color.WHITE)
                binding.root.postDelayed({
                    binding.root.foreground = null
                }, 100)
            }, 50)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!checkPermissions()) {
            findNavController().popBackStack()
        }
    }
}