package com.example.jetpack.repository.remote

import com.example.jetpack.repository.local.Product
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/payment/product/productList")
    suspend fun productList(): Response<BaseResult<List<Product>>>

    @POST("/payment/app/active")
    @FormUrlEncoded
    suspend fun active(@FieldMap params: Map<String, String>)

    @POST("/payment/bigdata/add")
    @FormUrlEncoded
    suspend fun add(@FieldMap params: Map<String, String>)
}