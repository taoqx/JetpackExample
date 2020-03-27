package com.example.jetpack.utilities

import android.content.Context
import com.example.jetpack.repository.local.AppDatabase
import com.example.jetpack.repository.local.ProductRepository
import com.example.jetpack.viewmodels.ProductDetailFactory
import com.example.jetpack.viewmodels.ProductViewModelFactory

object InjectUtil {
    fun provideProductViewModelFactory(context: Context): ProductViewModelFactory {
        val repository = getProductRepository(context)
        return ProductViewModelFactory(repository)
    }

    private fun getProductRepository(context: Context): ProductRepository {
        return ProductRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).productDao()
        )
    }

    fun provideProductDetailViewModelFactory(context: Context): ProductDetailFactory {
        val repository = getProductRepository(context)
        return ProductDetailFactory(repository)
    }
}