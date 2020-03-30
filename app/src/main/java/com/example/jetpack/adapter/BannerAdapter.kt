package com.example.jetpack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpack.databinding.ItemBannerCardBinding
import com.example.jetpack.repository.local.BannerBean

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    var bannerDatas: MutableList<BannerBean>? = null

    class ViewHolder(val binding: ItemBannerCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: BannerBean?) {
            binding.bannerBean = bean
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBannerCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return bannerDatas?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bannerDatas?.get(position))
    }
}