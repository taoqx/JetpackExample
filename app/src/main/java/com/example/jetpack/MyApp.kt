package com.example.jetpack

import android.app.Application
import android.content.Context

class MyApp : Application() {

    companion object {
        var appContext: Context? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        appContext = base
    }

}