package com.kotlin.webapp.webviewapp

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)


        WebView.setWebContentsDebuggingEnabled(true)                        // STEP - 02
        myWebView.settings.setAppCachePath(this.applicationContext.cacheDir.absolutePath)
        myWebView.settings.allowFileAccess = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.setAppCacheEnabled(true)
        myWebView.settings.javaScriptEnabled = true                         // STEP - 03
        myWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        myWebView.addJavascriptInterface(JavaScriptInterface(), J_OBJ)      // STEP - 04
        myWebView.webChromeClient = object : WebChromeClient() {}           // STEP - 05
        myWebView.webViewClient = object : WebViewClient() {                // STEP - 06
            var err_type = 0

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if (myWebView.settings.cacheMode == WebSettings.LOAD_CACHE_ELSE_NETWORK && request?.url.toString() == BASE_URL) {
                    myWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    myWebView.loadUrl(INNER_URL)
                } else {
                    ktPage.visibility = View.GONE
                    toast("404 - Page Not Found")
                    err_type++
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {     // STEP - 07
                var javaScript: String = "javascript: try { jObj = eval($J_OBJ); } catch(err) { jObj = window; }"
                if (url == BASE_URL && myWebView.settings.cacheMode == WebSettings.LOAD_DEFAULT) {
                    myWebView.loadUrl(javaScript)
                    toast("Online Mode : Loading Live Page ...")
                } else if (url == BASE_URL && myWebView.settings.cacheMode == WebSettings.LOAD_CACHE_ELSE_NETWORK) {
                    myWebView.loadUrl(javaScript)
                    toast("Offline Mode : Loading Cached Page ...")
                } else if (url == INNER_URL && myWebView.settings.cacheMode == WebSettings.LOAD_NO_CACHE) {
                    myWebView.clearCache(true)
                    myWebView.loadUrl(javaScript)
                    if (err_type == 0)
                        toast("Offline Mode : Loading App Page ...")
                }
            }
        }

        if (!isNetworkAvailable) {
            myWebView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
        myWebView.loadUrl(BASE_URL)

        btn.setOnClickListener {
            sendToJS()
            msg.setText("")
        }

    } // End of fun onCreate()


    override fun onDestroy() {                                              // STEP - 09
        myWebView.removeJavascriptInterface(J_OBJ)
        super.onDestroy()
    } // End of fun onDestroy()


    private inner class JavaScriptInterface {                               // STEP - 10
        @JavascriptInterface
        fun dispKt(webMsg: String) {
            if (webMsg.isNullOrEmpty() || webMsg.isNullOrBlank())
                label.text = label.hint.toString()
            else
                label.text = webMsg
        }
    } // End of JavaScriptInterface class


    fun sendToJS() {                                                        // Step - 11
        myWebView.evaluateJavascript("javascript: "
                +"dispJS('"+msg.text+"');"
                ,null
        )
    }


    companion object {                                                      // STEP - 01
        private val J_OBJ = "KT"
        private val INNER_URL = "file:///android_asset/WebViewApp/index.html"
        private val BASE_URL = "https://nishantsinghdev.github.io/WebViewApp/"
    } // End of Companion Object

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    fun toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this.applicationContext, message, duration).show()
    }

}
