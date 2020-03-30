package com.example.jetpack.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.jetpack.repository.local.BannerBean
import com.example.jetpack.repository.local.Product
import com.example.jetpack.repository.local.HomePageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageViewModel internal constructor(private val repository: HomePageRepository) :
    ViewModel() {

    private val _products: LiveData<PagedList<Product>> = repository.getProducts()
    var products: LiveData<PagedList<Product>> = _products

    private val _banners: LiveData<List<BannerBean>> = repository.getBanners()
    var banners: LiveData<List<BannerBean>> = _banners

    suspend fun refresh() {
        repository.requestData()
    }
}
