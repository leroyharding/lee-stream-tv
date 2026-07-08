package com.example.leestreamtv

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.VideoView

class MainActivity : Activity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make app immersive/fullscreen on TVs
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        webView = WebView(this).apply {
            // Enable JavaScript and storage APIs
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

            // Ensure proper WebView behavior for relative paths and local resources
            webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return false
                }
            }
            webChromeClient = WebChromeClient()

            // Enable focus for D-Pad Remote controls
            isFocusable = true
            isFocusableInTouchMode = true
            
            // Add JavaScript interfaces
            val bridge = WebAppInterface(this@MainActivity)
            addJavascriptInterface(bridge, "LeeStreamTVBridge")
            addJavascriptInterface(bridge, "LeePrimeBridge")
        }

        val rootLayout = FrameLayout(this)
        rootLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootLayout.addView(webView)

        val videoView = VideoView(this)
        videoView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splash_video}")
        videoView.setVideoURI(videoUri)
        
        videoView.setOnCompletionListener {
            rootLayout.removeView(videoView)
        }
        
        videoView.setOnErrorListener { _, _, _ ->
            rootLayout.removeView(videoView)
            true
        }

        rootLayout.addView(videoView)

        setContentView(rootLayout)
        webView.loadUrl("file:///android_asset/index.html")
        webView.requestFocus()
        
        videoView.start()
    }

    private var lastResumeTime: Long = 0

    override fun onResume() {
        super.onResume()
        lastResumeTime = System.currentTimeMillis()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WebAppInterface.REQUEST_CODE_EXTERNAL_PLAYER) {
            // Try to extract the playback position from the external player's result
            var positionMs: Long = -1
            var durationMs: Long = -1

            if (data != null) {
                // MX Player returns: position (int ms), duration (int ms), end_by (String)
                if (data.hasExtra("position")) {
                    positionMs = data.getIntExtra("position", -1).toLong()
                }
                if (data.hasExtra("duration")) {
                    durationMs = data.getIntExtra("duration", -1).toLong()
                }

                // VLC returns: extra_position (long ms), extra_duration (long ms)
                if (data.hasExtra("extra_position")) {
                    positionMs = data.getLongExtra("extra_position", -1)
                }
                if (data.hasExtra("extra_duration")) {
                    durationMs = data.getLongExtra("extra_duration", -1)
                }
            }

            if (positionMs > 0) {
                // Convert ms to seconds for the JavaScript side
                val positionSec = positionMs / 1000.0
                val durationSec = if (durationMs > 0) durationMs / 1000.0 else 0.0

                webView.evaluateJavascript(
                    "javascript:if(typeof window.onExternalPlayerResult === 'function') { window.onExternalPlayerResult($positionSec, $durationSec); }",
                    null
                )
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastResumeTime < 500) {
                return true
            }
            webView.evaluateJavascript("javascript:if(typeof window.handleAndroidBackPress === 'function') { window.handleAndroidBackPress(); } else { false; }") { result ->
                if (result == "false" || result == "null") {
                    runOnUiThread {
                        showExitDialog()
                    }
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showExitDialog() {
        android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            .setTitle("Exit LeeStreamTV")
            .setMessage("Do you want to exit the app?")
            .setPositiveButton("Exit") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
