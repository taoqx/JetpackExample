package com.example.jetpack.repository.remote

import android.os.Build
import com.example.jetpack.MyApp
import com.example.jetpack.utilities.SP_ACTIVE
import com.example.jetpack.utilities.getImei
import com.example.jetpack.utilities.getSpBoolean
import com.example.jetpack.utilities.putSpBoolean

class HttpLog {
    companion object {
        suspend fun active() {
            try {
                val context =  MyApp.appContext!!
                if (!getSpBoolean(
                        context,
                        SP_ACTIVE, false
                    )
                ) {
                    ApiServiceFactory.getService().active(
                        mapOf(
                            "imei" to (getImei(context)
                                ?: "imei"),
                            "package_name" to context.packageName,
                            "source_id" to "GooglePlay",
                            "system" to "Android",
                            "model_type" to Build.MODEL,
                            "brand_type" to Build.BRAND
                        )
                    )
                    putSpBoolean(
                        context,
                        SP_ACTIVE,
                        true
                    )
                }
            } catch (e: Exception) {
            }
        }

        suspend fun event( event: String, productName: String = "") {
            try {
                val context =  MyApp.appContext!!
                ApiServiceFactory.getService().add(
                    mapOf(
                        "event_name" to event,
                        "product_name" to productName,
                        "imei" to (getImei(context.applicationContext)
                            ?: "imei"),
                        "package_name" to context.packageName,
                        "source_id" to "GooglePlay",
                        "system" to "Android",
                        "model_type" to Build.MODEL,
                        "brand_type" to Build.BRAND
                    )
                )
            } catch (ex: Exception) {
            }

        }

        const val EVENT_INDEX_LOAD = "index_load"
        const val EVENT_CLICK_APPLY = "click_apply"
        const val EVENT_CLICK_DETAIL = "click_detail"
    }


}

