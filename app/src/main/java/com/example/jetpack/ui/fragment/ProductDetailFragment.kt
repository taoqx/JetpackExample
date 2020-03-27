package com.example.jetpack.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentProductDetailBinding
import com.example.jetpack.repository.remote.HttpLog
import com.example.jetpack.ui.view.EligibilityView
import com.example.jetpack.utilities.InjectUtil
import com.example.jetpack.utilities.dp2px
import com.example.jetpack.utilities.loadWeb
import com.example.jetpack.viewmodels.ProductDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailFragment : DialogFragment() {

    private val viewModel by viewModels<ProductDetailViewModel> {
        InjectUtil.provideProductDetailViewModelFactory(requireContext())
    }

    private lateinit var binding: FragmentProductDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_product_detail,
            null,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context ?: return binding.root
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.productName.value = arguments?.getString(PRODUCT)
        binding.lifecycleOwner = viewLifecycleOwner
        subscribeUI(binding)
    }

    private fun subscribeUI(binding: FragmentProductDetailBinding) {
        binding.setBackClickListener { dismiss() }

        viewModel.product.observe(viewLifecycleOwner, Observer {
            binding.product = it
            binding.setButtonClickListener { _ ->
                loadWeb(
                    it.android_jump_url
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    HttpLog.event(
                        HttpLog.EVENT_CLICK_DETAIL,
                        binding.product!!.name
                    )
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    delay(200)
                    withContext(Dispatchers.Main){
                        binding.llEligibility.removeAllViews()
                        it?.eligibility?.forEach { text ->
                            val view = EligibilityView(requireContext()).setText(text)
                            val params = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            params.bottomMargin = dp2px(context?.applicationContext!!, 5)
                            view.layoutParams = params
                            binding.llEligibility.addView(view)
                        }
                    }
                }
            }


        })
    }

    companion object {
        const val PRODUCT = "product"
        const val tag = "ProductDetailFragment"
        fun instance(name: String): ProductDetailFragment {
            val instance = ProductDetailFragment()
            instance.arguments = bundleOf(PRODUCT to name)
            return instance
        }
    }
}