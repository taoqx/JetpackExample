package com.example.jetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.jetpack.R
import com.example.jetpack.databinding.ItemBannerHomepageBinding
import com.example.jetpack.databinding.ItemHomeListBinding
import com.example.jetpack.databinding.ItemPrivacyBinding
import com.example.jetpack.repository.local.BannerBean
import com.example.jetpack.repository.local.Product
import com.example.jetpack.repository.remote.HttpLog
import com.example.jetpack.ui.fragment.ProductDetailFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductAdapter(private val manager: FragmentManager) :
    PagedListAdapter<Product, ViewHolder>(object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }) {

    class ProductViewHolder(
        private val binding: ItemHomeListBinding,
        private val manager: FragmentManager
    ) :
        ViewHolder(binding.root) {

        init {
            binding.setItemClickListener {
                showDetail(binding.product!!.name)
                GlobalScope.launch {
                    HttpLog.event(
                        HttpLog.EVENT_CLICK_APPLY,
                        binding.product!!.name
                    )
                }
            }
        }

        private fun showDetail(name: String) {
            ProductDetailFragment.instance(name).show(manager, ProductDetailFragment.tag)
        }

        fun bind(item: Product) {
            binding.product = item
        }

    }

    class PrivacyViewHolder(binding: ItemPrivacyBinding) : ViewHolder(binding.root) {
        init {
            binding.setItemClickListener { view ->
                view.findNavController().navigate(R.id.action_homeFragment_to_privacyFragment)
            }
        }
    }

    class BannerViewHolder(val binding: ItemBannerHomepageBinding) : ViewHolder(binding.root) {
        fun bind(bannerDatas: MutableList<BannerBean>) {
            (binding.viewPager.adapter as BannerAdapter).bannerDatas = bannerDatas
        }

        init {
            binding.viewPager.apply {
                adapter = BannerAdapter()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            BOTTOM -> {
                PrivacyViewHolder(
                    ItemPrivacyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            BANNER -> {
                BannerViewHolder(
                    ItemBannerHomepageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ProductViewHolder(
                    ItemHomeListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    manager
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> {
                holder.bind(bannerDatas)
            }
            is ProductViewHolder -> {
                holder.bind(getItem(position - 1) as Product)
            }
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> BANNER
            itemCount - 1 -> BOTTOM
            else -> super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 2 // banner + bottom
    }

    companion object {
        const val BOTTOM = 100
        const val BANNER = 101
    }

    var bannerDatas = mutableListOf<BannerBean>()
}
