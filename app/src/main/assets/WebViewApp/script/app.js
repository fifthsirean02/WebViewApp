var label = document.getElementById('label').innerHTML;


function sendToKT() {                                 // Calling Injected Kotlin function
  var msg = document.getElementById('msg').value;
  KT.dispKt(msg);
}

function dispJS(msg) {                                // Called By Kotlin
  if (msg=="")
    msg = label;
  document.getElementById('label').innerHTML = msg;
}

/*

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//                           JavaScript Functions
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

var label = document.getElementById('label').innerHTML;

dispJS(msg) {
  if (msg=="")
    msg = label;
  document.getElementById('label').innerHTML = msg;
}

function sendToKt() {
  var msg = document.getElementById('msg').value;
  KT.dispKt(msg);         // Calling Kotlin fun with Object : KT
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -




// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//                            Kotlin Functions
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

// KT Function for Being Called By JS Function (with Object 'KT') :
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
private inner class JavaScriptInterface {
  @JavascriptInterface
  fun dispKt(webMsg: String) {
    if (webMsg.isNullOrEmpty() || webMsg.isNullOrBlank())
      label.text = label.hint.toString()
    else
      label.text = webMsg
  }
}

// Kt Function for calling JS Function :
// - - - - - - - - - - - - - - - - - -
fun sendToJS() {
  myWebView.evaluateJavascript("javascript: "
      +"dispJS('"+msg.text+"');"
    ,null
  )
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

*/
