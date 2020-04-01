package com.example.jetpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.jetpack.databinding.ActivityMainBinding
import com.example.jetpack.repository.remote.HttpLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                HttpLog.active()
            }
            withContext(Dispatchers.IO) {
                HttpLog.event(HttpLog.EVENT_INDEX_LOAD)
            }
        }
    }

}
