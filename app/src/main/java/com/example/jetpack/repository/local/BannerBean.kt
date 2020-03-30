package com.example.jetpack.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerBean(@PrimaryKey(autoGenerate = true) val id: Long, val url: String, val destination: String)