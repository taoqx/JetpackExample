package com.example.jetpack.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentPrivacyBinding
import com.example.jetpack.utilities.StatusBarUtil


class PrivacyFragment : Fragment() {
    private lateinit var binding: FragmentPrivacyBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_privacy,
            null,
            false
        )
        binding.webView.loadUrl("file:///android_asset/policy.html")
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        StatusBarUtil.setGradientColor(requireActivity(), binding.toolbar)
        return binding.root
    }


}