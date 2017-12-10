package com.kotlin.webapp.webviewapp

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.activity_kotlin.*
import android.os.Build
import android.webkit.WebChromeClient
import android.widget.Toast


class KotlinActivity_Old : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {      // Verifying OS Version >= KitKat

            if (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
                WebView.setWebContentsDebuggingEnabled(true)

        } // End of if() condition

        myWebView.settings.javaScriptEnabled = true                     // STEP - 02
        myWebView.addJavascriptInterface(JavaScriptInterface(), J_OBJ)  // STEP - 03
        myWebView.webChromeClient = object : WebChromeClient() {}       // STEP - 04
        myWebView.webViewClient = object : WebViewClient() {            // STEP - 05

            override fun onPageFinished(view: WebView?, url: String?) {
                if (url == BASE_URL || url == INNER_URL) {
                    var javaScript: String = "javascript: try { jObj = eval($J_OBJ); } catch(err) { jObj = window; }"
                    myWebView.loadUrl(javaScript)
                    injectJavaScriptFunction()
                }
            } // End of fun onPageFinished()

        } // End of fun WebViewClient()

        if (isNetworkAvailable) {                                       // STEP - 06
            myWebView.loadUrl(BASE_URL)
            toast("Online Mode : Loading Live Page ...")
        } else {
            myWebView.loadUrl(INNER_URL)
            toast("Offline Mode : Loading App Page ...")
        }

        btn.setOnClickListener {
            msgToJS()
            msg.setText("")
        }

    } // End of fun onCreate()


    override fun onDestroy() {                                          // STEP - 07
        myWebView.removeJavascriptInterface(J_OBJ)
        super.onDestroy()
    } // End of fun onDestroy()


    private inner class JavaScriptInterface {                           // STEP - 08
        @JavascriptInterface
        fun msgFromJS(webMsg: String) {
            if (webMsg.isNullOrEmpty() || webMsg.isNullOrBlank())
                label.text = label.hint.toString()
            else
                label.text = webMsg
        }
    } // End of JavaScriptInterface class


    private fun injectJavaScriptFunction() {                            // STEP - 09
        myWebView.loadUrl("javascript: "
                +"window.androidObj.msgToKT = function(msg) { "
                + J_OBJ + ".msgFromJS(msg) " +
                " }"
        )
    } // End of fun injectJavaScriptFunction()


    fun msgToJS() {                                                     // STEP - 10
        myWebView.evaluateJavascript("javascript: "
                +"msgFromKT(\""+msg.text+"\");"
                ,null
        )
    } // End of fun msgToJS()


    companion object {                                                  // STEP - 01
        private val J_OBJ = "JAVASCRIPT_OBJ"
        private val INNER_URL = "file:///android_asset/WebViewApp/index.html"
        private val BASE_URL = "https://fifthsirean02.github.io/WebViewApp/"
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
