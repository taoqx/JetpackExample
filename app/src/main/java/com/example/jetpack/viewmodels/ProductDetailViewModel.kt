package com.example.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.jetpack.repository.local.ProductRepository

class ProductDetailViewModel(repository: ProductRepository) : ViewModel() {
    val productName: MutableLiveData<String> = MutableLiveData()
    val product = productName.switchMap {
        repository.getProduct(it)
    }
}