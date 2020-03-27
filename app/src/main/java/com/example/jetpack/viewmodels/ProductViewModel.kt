package com.example.jetpack.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.jetpack.repository.local.Product
import com.example.jetpack.repository.local.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel internal constructor(private val repository: ProductRepository) :
    ViewModel() {

    private val _products: LiveData<PagedList<Product>> = repository.getProducts()
    var products: LiveData<PagedList<Product>> = _products

    fun refresh() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.requestData()
            }
        }
    }
}
