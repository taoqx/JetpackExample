package com.example.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpack.repository.local.HomePageRepository

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(private val repository: HomePageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomePageViewModel(repository) as T
    }
}