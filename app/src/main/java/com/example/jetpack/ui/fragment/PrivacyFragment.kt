package com.example.jetpack.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentPrivacyBinding
import com.example.jetpack.utilities.StatusBarUtil


class PrivacyFragment : Fragment() {
    private lateinit var binding: FragmentPrivacyBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_privacy,
            null,
            false
        )
        initWebView(binding.webView, requireContext())
        binding.webView.loadUrl("file:///android_asset/policy.html")
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        StatusBarUtil.setGradientColor(requireActivity(), binding.toolbar)
        return binding.root
    }

    private fun initWebView(
        webview: WebView, context: Context
    ) {
        val webSettings: WebSettings = webview.getSettings()
        webSettings.javaScriptEnabled = true //让WebView支持JavaScript
        //        webSettings.setDomStorageEnabled(true);     //启用H5 DOM API （默认false）
//        webSettings.setDatabaseEnabled(true);           //启用数据库api（默认false）可结合 setDatabasePath 设置路径
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT //设置缓存模式,一共有四种模式
        webSettings.setAppCacheEnabled(true) //启用应用缓存（默认false）可结合 setAppCachePath 设置缓存路径
        //        webSettings.setUserAgentString(usere);
//        webSettings.setAppCacheMaxSize(1);        //已过时，高版本API上，系统会自行分配
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);         //提高渲染的优先级
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.setSupportZoom(true) //支持缩放，默认为true
        webSettings.displayZoomControls = false //隐藏原生的缩放控件,但是还是支持缩放功能
        //        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);      //支持内容重新布局
//        webSettings.supportMultipleWindows();       //支持多窗口
        webSettings.allowFileAccess = true //设置可以访问文件
        //        webSettings.setNeedInitialFocus(true);      //当webview调用requestFocus时为webview设置节点
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //自动加载图片
        webSettings.defaultTextEncodingName = "UTF-8" //设置编码格式
        webSettings.blockNetworkImage = false
        webSettings.domStorageEnabled = true //开启DOM storage API功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW //
        }

        //支持定位的
        webSettings.databaseEnabled = true
        val dir =
            context.getDir("database", Context.MODE_PRIVATE).path
        webSettings.setGeolocationEnabled(true)
        webSettings.setAppCachePath(dir)
        val appCachePath = context.cacheDir.absolutePath
        webSettings.setGeolocationDatabasePath(appCachePath)
        //支持拍照和图片选择的
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        CookieManager.getInstance().setAcceptCookie(true)
    }
}