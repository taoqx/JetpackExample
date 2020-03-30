package com.example.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.jetpack.repository.local.HomePageRepository

class ProductDetailViewModel(repository: HomePageRepository) : ViewModel() {
    val productName: MutableLiveData<String> = MutableLiveData()
    val product = productName.switchMap {
        repository.getProduct(it)
    }
}