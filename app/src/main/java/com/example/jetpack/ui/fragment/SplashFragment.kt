package com.example.jetpack.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jetpack.databinding.FragmentSplashBinding
import com.example.jetpack.viewmodels.SplashViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val requestCode = 101
    private val delayTime = 1000L
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    private val splashViewModel: SplashViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // do nothing when called pop back
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.filter {
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }.apply {
                if (size > 0)
                    requestPermissions(
                        this.toTypedArray(),
                        requestCode
                    )
                else
                    popBack()
            }
        } else {
            popBack()
        }
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun popBack() {
        GlobalScope.launch {
            delay(delayTime)
            splashViewModel.showed = true
            findNavController().popBackStack()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        popBack()
    }
}