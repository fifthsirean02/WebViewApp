package com.kotlin.webapp.webviewapp

import android.content.pm.ApplicationInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.activity_kotlin.*
import android.os.Build


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
        myWebView.webViewClient = object : WebViewClient() {            // STEP - 04

            override fun onPageFinished(view: WebView?, url: String?) {
                if (url == BASE_URL)
                    injectJavaScriptFunction()
            } // End of fun onPageFinished()

        } // End of fun WebViewClient()

        myWebView.loadUrl(BASE_URL)                                     // STEP - 05

        btn.setOnClickListener {
            msgToJS()
        }

    } // End of fun onCreate()


    override fun onDestroy() {                                          // STEP - 06
        myWebView.removeJavascriptInterface(J_OBJ)
        super.onDestroy()
    } // End of fun onDestroy()


    private inner class JavaScriptInterface {                           // STEP - 07
        @JavascriptInterface
        fun msgFromJS(webMsg: String) {
            if (webMsg.isNullOrEmpty() || webMsg.isNullOrBlank())
                label.text = label.hint.toString()
            else
                label.text = webMsg
        }
    } // End of JavaScriptInterface class


    private fun injectJavaScriptFunction() {                            // STEP - 08
        myWebView.loadUrl("javascript: "
                +"window.androidObj.msgToKT = function(msg) { "
                + J_OBJ + ".msgFromJS(msg) " +
                " }"
        )
    } // End of fun injectJavaScriptFunction()


    fun msgToJS() {                                                     // STEP - 09
        myWebView.evaluateJavascript("javascript: "
                +"msgFromKT(\""+msg.text+"\");"
                ,null
        )
    } // End of fun msgToJS()


    companion object {                                                  // STEP - 01
        private val J_OBJ = "JAVASCRIPT_OBJ"
        private val BASE_URL = "file:///android_asset/WebViewApp/index.html"
    } // End of Companion Object

}
