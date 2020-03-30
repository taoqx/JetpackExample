package com.example.jetpack.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BannerDao {
    @Query("SELECT * FROM banners")
    fun queryAll(): LiveData<List<BannerBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(beans: List<BannerBean>)
}