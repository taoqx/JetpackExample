package com.example.jetpack.repository.local

import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.jetpack.repository.remote.ApiServiceFactory
import com.example.jetpack.repository.remote.BaseResult

class HomePageRepository private constructor(
    private val productDao: ProductDao,
    private val bannerDao: BannerDao
) {

    fun getProducts(): LiveData<PagedList<Product>> {
        val pageConfig = Config(
            pageSize = 10,
            prefetchDistance = 150,
            enablePlaceholders = true
        )
        return productDao.queryAll().toLiveData(pageConfig)
    }


    suspend fun requestData() {
        try {
            val response = ApiServiceFactory.getService().productList()
            if (response.isSuccessful && response.body() != null) {
                val result = response.body() as BaseResult<List<Product>>
                productDao.deleteAll()
                productDao.insertAll(result.data)
            }
        } catch (ex: java.lang.Exception) {
        }
    }

    fun getProduct(name: String) = productDao.queryByName(name)

    fun getBanners() = bannerDao.queryAll()

    companion object {
        @Volatile
        private var instance: HomePageRepository? = null

        fun getInstance(productDao: ProductDao, bannerDao: BannerDao): HomePageRepository {
            return instance
                ?: synchronized(this) {
                    instance
                        ?: HomePageRepository(productDao, bannerDao)
                            .also { instance = it }
                }
        }
    }
}