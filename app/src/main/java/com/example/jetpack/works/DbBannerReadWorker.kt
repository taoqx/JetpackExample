package com.example.jetpack.works

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jetpack.repository.local.AppDatabase
import com.example.jetpack.repository.local.BannerBean
import com.example.jetpack.repository.local.BannerDao
import com.example.jetpack.repository.local.Product
import com.example.jetpack.utilities.TEST_JSON
import com.example.jetpack.utilities.TEST_JSON_BANNER
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope

class DbBannerReadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(TEST_JSON_BANNER).use { inputStream ->
                com.google.gson.stream.JsonReader(inputStream.reader()).use { jsonReader ->
                    val plantType = object : TypeToken<List<BannerBean>>() {}.type
                    val plantList: List<BannerBean> = Gson().fromJson(jsonReader, plantType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.bannerDao().insertAll(plantList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG,"Error read database ${ex.message}")
            Result.failure()
        }
    }

    companion object {
        private val TAG = DbBannerReadWorker::class.java.simpleName
    }
}