package com.example.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentUserCenterBinding
import com.example.jetpack.utilities.StatusBarUtil
import com.example.jetpack.viewmodels.LoginViewModel

class UserCenterFragment : Fragment() {
    lateinit var binding: FragmentUserCenterBinding
    private val viewModel by activityViewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_center, container, false)
        StatusBarUtil.setGradientColor(requireActivity(), binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoginViewModel.LoginState.AUTHENTICATED -> showUsername()
                LoginViewModel.LoginState.UNAUTHENTICATED -> {
                    findNavController().navigate(R.id.action_userCenterFragment_to_loginFragment)
                }
                else -> {}
            }
        })
    }

    private fun showUsername() {
        binding.username = viewModel.username
    }
}