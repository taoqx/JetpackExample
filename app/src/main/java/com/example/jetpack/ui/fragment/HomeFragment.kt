package com.example.jetpack.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpack.R
import com.example.jetpack.adapter.ProductAdapter
import com.example.jetpack.databinding.FragmentHomeBinding
import com.example.jetpack.utilities.InjectUtil
import com.example.jetpack.utilities.StatusBarUtil
import com.example.jetpack.utilities.jumpToFacebook
import com.example.jetpack.viewmodels.HomePageViewModel
import com.example.jetpack.viewmodels.SplashViewModel


class HomeFragment : Fragment() {

    private val splashViewModel by activityViewModels<SplashViewModel>()

    private val viewModel by activityViewModels<HomePageViewModel> {
        InjectUtil.provideProductViewModelFactory(requireContext())
    }

    private var binding: FragmentHomeBinding? = null
    private var adapter: ProductAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!splashViewModel.showed) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSplashFragment())
        }

        binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.fragment_home,
                null,
                false
            )

        //swipe refresh
        binding!!.swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        binding!!.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refresh()
        binding!!.swipeRefresh.isRefreshing = true

        //recycler view
        adapter = ProductAdapter(childFragmentManager).also {
            // fix a bug from PagedListAdapter : 多类型item时加载时滚动到奇怪位置
            it.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    if (positionStart == 0) {
                        binding?.recyclerView?.layoutManager?.scrollToPosition(0)
                    }
                }
            })
        }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.apply {
            itemAnimator?.addDuration = 0
            itemAnimator?.changeDuration = 0
            itemAnimator?.moveDuration = 0
            itemAnimator?.removeDuration = 0
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }

        //toolbar
        binding!!.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.contact_us -> {
                        jumpToFacebook()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setGradientColor(requireActivity(), binding!!.toolbar)
        binding!!.lifecycleOwner = viewLifecycleOwner
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.products.observe(viewLifecycleOwner, Observer {
            adapter?.submitList(it)
            binding?.swipeRefresh?.isRefreshing = false
        })

        viewModel.banners.observe(viewLifecycleOwner, Observer {
            Log.d("qingxin", "${it.size}")
            adapter?.bannerDatas = it.toMutableList()
            adapter?.notifyDataSetChanged()
        })
    }

}