package com.example.jetpack.repository.remote

data class BaseResult<T>(val code: Int, val message: String, val data: T)