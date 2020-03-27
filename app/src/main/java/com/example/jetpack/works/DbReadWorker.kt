package com.example.jetpack.works

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jetpack.repository.local.AppDatabase
import com.example.jetpack.repository.local.Product
import com.example.jetpack.utilities.TEST_JSON
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope

class DbReadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(TEST_JSON).use { inputStream ->
                com.google.gson.stream.JsonReader(inputStream.reader()).use { jsonReader ->
                    val plantType = object : TypeToken<List<Product>>() {}.type
                    val plantList: List<Product> = Gson().fromJson(jsonReader, plantType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.productDao().insertAll(plantList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG,"Error read database ${ex.message}")
            Result.failure()
        }
    }

    companion object {
        private val TAG = DbReadWorker::class.java.simpleName
    }
}