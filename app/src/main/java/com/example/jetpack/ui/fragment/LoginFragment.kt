package com.example.jetpack.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentLoginBinding
import com.example.jetpack.utilities.StatusBarUtil
import com.example.jetpack.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        StatusBarUtil.setGradientColor(requireActivity(), binding.toolbar)

        //back to home insteadof user-center
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoginViewModel.LoginState.AUTHENTICATED -> findNavController().popBackStack()
                else -> {
                }
            }
        })
        viewModel.avatar.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(Uri.fromFile(it))
                .apply(RequestOptions.circleCropTransform())
                .into(binding.avatar)
        })

        binding.loginButton.setOnClickListener {
            viewModel.login(binding.editName.text.toString())
        }

        binding.avatar.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCameraFragment())
        }
    }


}