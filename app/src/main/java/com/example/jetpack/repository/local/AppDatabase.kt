package com.example.jetpack.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.jetpack.utilities.DATABASE_NAME
import com.example.jetpack.works.DbBannerReadWorker
import com.example.jetpack.works.DbReadWorker

@Database(entities = [Product::class, BannerBean::class], version = 1, exportSchema = false)
@TypeConverters(EligibilityConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun bannerDao(): BannerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    )
                        .also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<DbReadWorker>().build()
                        val bannerRequest = OneTimeWorkRequestBuilder<DbBannerReadWorker>().build()

                        WorkManager.getInstance(context).enqueue(listOf(bannerRequest, request))
                    }
                })
                .build()
        }
    }
}