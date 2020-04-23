package com.example.jetpack.utilities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.jetpack.MyApp
import java.text.NumberFormat
import java.util.*

/**
 * 注册位置服务
 */
fun requestLocation(context: Context, listener: LocationListener) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)
    manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
}

/**
 * 移除位置服务
 */
fun removeLocation(context: Context, listener: LocationListener) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    manager.removeUpdates(listener)
}

/**
 * 跳转手机相册
 */
private fun jumpPhotoAlbum(context: Context) {
    val intent = Intent()
    intent.action = Intent.ACTION_PICK
    intent.type = "image/*"
    context.startActivity(intent)
}

/**
 * 调用系统浏览器 加载网页
 */
fun loadWeb(url: String?) {
    if (TextUtils.isEmpty(url)) return
    val uri: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    MyApp.appContext!!.startActivity(intent)
}

/**
 * 获取手机的IMEI
 *
 * @return
 */
fun getImei(context: Context): String? {
    var deviceId: String? = ""
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val tm: TelephonyManager?
        deviceId = try {
            tm = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.deviceId
        } catch (e: Exception) {
            ""
        }
    }
    if (TextUtils.isEmpty(deviceId)) {
        deviceId = getUUID(context)
    }
    return deviceId
}

/**
 * 根据硬件信息生成一个唯一的 UUID 来保持唯一性和持久性
 *
 * @return
 */
fun getUUID(context: Context): String? {
    var serial = ""
    val m_szDevIDShort =
        "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 位
    try {
        serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Build.getSerial()
            }
            Build.SERIAL
        } else {
            Build.SERIAL
        }
        //API>=9 使用serial号
        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    } catch (exception: Exception) {
        //serial需要一个初始化
        serial = "serial" // 随便一个初始化
    }
    //使用硬件信息拼凑出来的15位号码
    return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
}

/**
 * 检查网络是否可用
 */
fun isNetworkAvailable(context: Context): Boolean {
    val manager = context.applicationContext.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val info = manager.activeNetworkInfo
    return info != null && info.isAvailable
}

/**
 * 将数字字符串转换为带千分号格式
 */
fun number2Thousand(num: String): String {
    var numStr = ""
    if (TextUtils.isEmpty(num))
        return numStr
    var format = NumberFormat.getNumberInstance()
    try {
        numStr = format.format(num.toLong())
    } catch (e: Exception) {
        Log.e("number2Thousand", e.message)
    }
    return numStr
}

/**
 * dp转换px
 */
fun dp2px(context: Context, value: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (value * scale + 0.5f).toInt()
}

fun screenWidth(context: Context) = context.resources.displayMetrics.widthPixels
fun screenHeight(context: Context) = context.resources.displayMetrics.heightPixels

/**
 * 跳转到其他App
 */
fun jumpToApp() {
    val manager = MyApp.appContext!!.packageManager
    val intent = manager.getLaunchIntentForPackage("com.facebook.katana")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data = Uri.parse("fb://facewebmodal/f?href=$FACEBOOK")
    MyApp.appContext!!.startActivity(intent)
}

/**
 * 打开facebook联系客服
 */
fun jumpToFacebook() {
    val context = MyApp.appContext!!
    val packageName = "com.facebook.katana"
    var intent = if (context.packageManager.getLaunchIntentForPackage(packageName) == null) {
        Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK))
    } else {
        Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$FACEBOOK"))
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

/**
 * 获得状态栏高度
 */
fun statusHeight(): Int {
    val resourceId: Int =
        MyApp.appContext!!.resources.getIdentifier("status_bar_height", "dimen", "android")
    return MyApp.appContext!!.resources.getDimensionPixelSize(resourceId)
}