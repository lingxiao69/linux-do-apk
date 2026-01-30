/*
 * Linux Do WebView App
 * 
 * A lightweight Android app that wraps linux.do website in a native WebView.
 * 
 * Features:
 * - Cookie persistence for login
 * - Pull-to-refresh
 * - Loading progress bar
 * - Hardware acceleration
 * 
 * License: MIT
 */
package com.linuxdo.app

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Main activity that hosts the WebView for linux.do
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    companion object {
        /** Target website URL - change this to wrap a different website */
        private const val TARGET_URL = "https://linux.do"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupCookieManager()
        setupWebView()
        setupSwipeRefresh()

        webView.loadUrl(TARGET_URL)
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        swipeRefresh = findViewById(R.id.swipeRefresh)
    }

    private fun setupCookieManager() {
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            // Enable JavaScript and storage
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true

            // Cache settings - prefer cache for faster loading
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

            // Viewport settings
            useWideViewPort = true
            loadWithOverviewMode = true
            
            // Text size - reduce for more compact display
            textZoom = 85  // 85% of default, shows more content per screen

            // Zoom controls
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false

            // Allow mixed content (HTTP resources on HTTPS pages)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // Enable hardware acceleration for smoother scrolling
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                // Handle all URLs within the WebView
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 0
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                // Persist cookies after page load
                CookieManager.getInstance().flush()
                
                // Inject CSS to enhance mobile UI
                val css = """
                    /* 更紧凑的帖子列表 */
                    .topic-list-item { 
                        padding: 6px 0 !important; 
                        border-bottom: 1px solid #eee !important;
                    }
                    .topic-list .topic-list-data {
                        padding: 4px 5px !important;
                    }
                    
                    /* 标题样式优化 */
                    .topic-list-item .title {
                        font-size: 15px !important;
                        font-weight: 600 !important;
                        line-height: 1.3 !important;
                    }
                    
                    /* 分类标签更紧凑 */
                    .badge-category, .category-name {
                        font-size: 11px !important;
                        padding: 1px 4px !important;
                    }
                    .discourse-tag {
                        font-size: 11px !important;
                        padding: 1px 4px !important;
                        margin: 1px !important;
                    }
                    
                    /* 强制显示回复数和浏览量 */
                    .topic-list .posts, .topic-list .views, .topic-list .activity {
                        display: table-cell !important;
                        font-size: 12px !important;
                        color: #666 !important;
                    }
                    
                    /* 头像更小 */
                    .avatar {
                        width: 24px !important;
                        height: 24px !important;
                    }
                    
                    /* 公告横幅更紧凑 */
                    .alert {
                        padding: 6px 10px !important;
                        font-size: 13px !important;
                    }
                """.trimIndent()
                val js = "javascript:(function(){" +
                        "var style = document.createElement('style');" +
                        "style.innerHTML = '${css.replace("\n", " ").replace("'", "\\'")}'; " +
                        "document.head.appendChild(style);" +
                        "})()"
                view?.loadUrl(js)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setColorSchemeColors(0xFF2196F3.toInt())
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }

        // Disable swipe refresh when not at top of page
        webView.viewTreeObserver.addOnScrollChangedListener {
            swipeRefresh.isEnabled = webView.scrollY == 0
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
        // Persist cookies when app goes to background
        CookieManager.getInstance().flush()
    }
}
