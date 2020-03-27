package com.example.jetpack.utilities

import android.content.Context
import android.content.SharedPreferences

fun getSp(context: Context): SharedPreferences =
    context.applicationContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

fun putSpBoolean(context: Context, key: String, value: Boolean) =
    getSp(context).edit().putBoolean(key, value).apply()

fun getSpBoolean(context: Context, key: String, default: Boolean) =
    getSp(context).getBoolean(key, default)