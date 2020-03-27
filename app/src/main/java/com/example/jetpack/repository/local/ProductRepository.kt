package com.example.jetpack.repository.local

import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.jetpack.repository.remote.ApiServiceFactory
import com.example.jetpack.repository.remote.BaseResult

class ProductRepository private constructor(private val dao: ProductDao) {

    fun getProducts(): LiveData<PagedList<Product>> {
        val pageConfig = Config(
            pageSize = 10,
            prefetchDistance = 150,
            enablePlaceholders = true
        )
        return dao.queryAll().toLiveData(pageConfig)
    }


    suspend fun requestData() {
        try {
            val response = ApiServiceFactory.getService().productList()
            if (response.isSuccessful && response.body() != null) {
                val result = response.body() as BaseResult<List<Product>>
                dao.deleteAll()
                dao.insertAll(result.data)
            }
        } catch (ex: java.lang.Exception) {
        }
    }

    fun getProduct(name: String) = dao.queryByName(name)

    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(dao: ProductDao): ProductRepository {
            return instance
                ?: synchronized(this) {
                    instance
                        ?: ProductRepository(dao)
                            .also { instance = it }
                }
        }
    }
}