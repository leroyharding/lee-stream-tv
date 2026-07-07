package com.example.leestreamtv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import android.widget.Toast
import java.io.File

class WebAppInterface(private val mContext: Context) {

    companion object {
        const val REQUEST_CODE_EXTERNAL_PLAYER = 1001
    }

    @JavascriptInterface
    fun isAvailable(): Boolean {
        return true
    }

    @JavascriptInterface
    fun playInMXPlayer(url: String, title: String) {
        playInMXPlayer(url, title, 0)
    }

    @JavascriptInterface
    fun playInMXPlayer(url: String, title: String, positionMs: Int) {
        val packages = listOf("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.pro")
        launchExternalPlayer(url, title, packages, "MX Player", positionMs.toLong())
    }

    @JavascriptInterface
    fun playInVLC(url: String, title: String) {
        playInVLC(url, title, 0)
    }

    @JavascriptInterface
    fun playInVLC(url: String, title: String, positionMs: Int) {
        launchExternalPlayer(url, title, listOf("org.videolan.vlc"), "VLC Player", positionMs.toLong())
    }

    @JavascriptInterface
    fun playInJustPlayer(url: String, title: String) {
        playInJustPlayer(url, title, 0)
    }

    @JavascriptInterface
    fun playInJustPlayer(url: String, title: String, positionMs: Int) {
        launchExternalPlayer(url, title, listOf("com.brouken.player"), "Just Player", positionMs.toLong())
    }

    @JavascriptInterface
    fun playInDefaultPlayer(url: String, title: String) {
        playInDefaultPlayer(url, title, 0)
    }

    @JavascriptInterface
    fun playInDefaultPlayer(url: String, title: String, positionMs: Int) {
        launchExternalPlayer(url, title, emptyList(), "Default Player", positionMs.toLong())
    }

    private fun launchExternalPlayer(url: String, title: String, packages: List<String>, displayName: String, positionMs: Long = 0) {
        var launched = false
        val sanitizedUrl = sanitizeUrl(url)
        val activity = mContext as? Activity

        for (pkg in packages) {
            try {
                val intent = createPlayerIntent(sanitizedUrl, title, pkg, positionMs)
                if (activity != null) {
                    activity.startActivityForResult(intent, REQUEST_CODE_EXTERNAL_PLAYER)
                } else {
                    mContext.startActivity(intent)
                }
                launched = true
                break
            } catch (e: Exception) {
                // Try next package
            }
        }

        if (!launched) {
            if (packages.isNotEmpty()) {
                try {
                    Toast.makeText(mContext, "$displayName not installed. Attempting default player...", Toast.LENGTH_SHORT).show()
                    val intent = createPlayerIntent(sanitizedUrl, title, "", positionMs)
                    if (activity != null) {
                        activity.startActivityForResult(intent, REQUEST_CODE_EXTERNAL_PLAYER)
                    } else {
                        mContext.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "No player could be launched: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                try {
                    val intent = createPlayerIntent(sanitizedUrl, title, "", positionMs)
                    if (activity != null) {
                        activity.startActivityForResult(intent, REQUEST_CODE_EXTERNAL_PLAYER)
                    } else {
                        mContext.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "No player could be launched: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createPlayerIntent(url: String, title: String, pkg: String, positionMs: Long): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(url), "video/*")
            if (pkg.isNotEmpty()) {
                setPackage(pkg)
            }
            putExtra("title", title)
            putExtra("displayName", title)

            // Pass resume position to external players (milliseconds)
            if (positionMs > 0) {
                putExtra("position", positionMs.toInt())       // MX Player & Just Player (Int ms)
                putExtra("from_start", false)                   // VLC: don't start from beginning
            }

            // Inject standard browser headers to bypass hotlinking protection
            val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            putExtra("headers", arrayOf("User-Agent: $userAgent")) // VLC format
            putExtra("headers", arrayOf("User-Agent", userAgent)) // MX Player format
            val headersBundle = android.os.Bundle().apply {
                putString("User-Agent", userAgent)
            }
            putExtra("extra_headers", headersBundle) // Bundle format

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    @JavascriptInterface
    fun downloadAndInstallAPK(apkUrl: String) {
        val activity = mContext as? android.app.Activity
        activity?.runOnUiThread {
            Toast.makeText(mContext, "Downloading update in background...", Toast.LENGTH_LONG).show()
        }

        try {
            val request = android.app.DownloadManager.Request(Uri.parse(apkUrl)).apply {
                setTitle("LeeStreamTV Update")
                setDescription("Downloading the latest release...")
                setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalFilesDir(mContext, android.os.Environment.DIRECTORY_DOWNLOADS, "LeeStreamTV_Update.apk")
            }

            val manager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
            val downloadId = manager.enqueue(request)

            val onComplete = object : android.content.BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        val file = File(mContext.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS), "LeeStreamTV_Update.apk")
                        if (file.exists()) {
                            val fileUri = androidx.core.content.FileProvider.getUriForFile(
                                mContext,
                                mContext.packageName + ".fileprovider",
                                file
                            )
                            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(fileUri, "application/vnd.android.package-archive")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            mContext.startActivity(installIntent)
                        } else {
                            activity?.runOnUiThread {
                                Toast.makeText(mContext, "Downloaded file not found.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        activity?.runOnUiThread {
                            Toast.makeText(mContext, "Installation failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    } finally {
                        try {
                            mContext.unregisterReceiver(this)
                        } catch (e: Exception) {
                            // Already unregistered
                        }
                    }
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                mContext.registerReceiver(
                    onComplete,
                    android.content.IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                    Context.RECEIVER_EXPORTED
                )
            } else {
                mContext.registerReceiver(
                    onComplete,
                    android.content.IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                )
            }
        } catch (e: Exception) {
            activity?.runOnUiThread {
                Toast.makeText(mContext, "Failed to start download: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    @JavascriptInterface
    fun openYoutubeTrailer(videoId: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
        
        val activity = mContext as? android.app.Activity
        try {
            mContext.startActivity(intentApp)
        } catch (e: Exception) {
            try {
                mContext.startActivity(intentWeb)
            } catch (ex: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(mContext, "Could not open YouTube: ${ex.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sanitizeUrl(url: String): String {
        return url
            .replace(" ", "%20")
            .replace("[", "%5B")
            .replace("]", "%5D")
            .replace("{", "%7B")
            .replace("}", "%7D")
            .replace("#", "%23")
    }
}
