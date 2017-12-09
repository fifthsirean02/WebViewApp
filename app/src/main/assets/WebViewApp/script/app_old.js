var jObj = window;        // Window Object to Manipulate from Kotlin
var label = document.getElementById('label').innerHTML;


setTimeout(function() {   // Show a Greet Message after 0.4 sec of loading JS
  jObj.setLabel();
}, 400);

function setLabel() {
  if (jObj == window)
    label = "Msg from Web Page...";
  else
    label = "Msg from Android...";

  document.getElementById('label').innerHTML = label;
}


window.androidObj = function AndroidClass(){};

function sendToKT() {                                 // Calling Injected Kotlin function
  var msg = document.getElementById('msg').value;
  document.getElementById('msg').value = "";
  window.androidObj.msgToKT(msg);
}

function msgFromKT(msg) {                             // Called By Kotlin
  if (msg=="")
    msg = label;
  document.getElementById('label').innerHTML = msg;
}

/*
Functions Injected By Kotlin in JS :-
- - - - - - - - - - - - - - - - - -

  1.) @JavascriptInterface
      fun msgFromJS(var msg) {                        // Kotlin Specific Code

        if (webMsg.isNullOrEmpty() || webMsg.isNullOrBlank())
          Msg = dmsg
        else
          Msg = webMsg

        label.text = Msg

      }

  2.) window.androidObj.msgToKT = function(msg) {     // Kotlin Specific Code
        J_OBJ.msgFromJS(msg);
      }
*/
