package com.example.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpack.repository.local.HomePageRepository

class ProductDetailFactory(private val repository: HomePageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductDetailViewModel(repository) as T
    }
}