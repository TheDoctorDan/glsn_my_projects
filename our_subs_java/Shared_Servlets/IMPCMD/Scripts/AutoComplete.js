/* AutoComplete.js */

/* include when using autocompletion fields */



function getElementY(element){
	var targetTop = 0;
	if (element.offsetParent) {
		while (element.offsetParent) {
			targetTop += element.offsetTop;
            element = element.offsetParent;
		}
	} else if (element.y) {
		targetTop += element.y;
    }
	return targetTop;
}



var autoComplete_isIE;







/*
Determine whether the browser is IE5.5.
*/
function autoComplete_func_isIE55() { // Private method
  return navigator.userAgent.indexOf("MSIE 5.5") > -1;
}

/*
Determine whether the browser is IE5.0 or IE5.5.
*/
function autoComplete_func_isIE5() { // Private method
  return navigator.userAgent.indexOf("MSIE 5") > -1;
}

/*
Determine whether the browser is IE6.
*/
function autoComplete_func_isIE6() { // Private method
  return navigator.userAgent.indexOf("MSIE 6") > -1 && navigator.userAgent.indexOf("Opera") == -1;
}

/*
Determine whether the browser is IE7.
*/
function autoComplete_func_isIE7() { // Private method
  return navigator.userAgent.indexOf("MSIE 7") > -1 && navigator.userAgent.indexOf("Opera") == -1;
}

/*
Determine whether the browser is IE.
*/
function autoComplete_func_isIE() { // Private method
  return autoComplete_func_isIE5() || autoComplete_func_isIE6() || autoComplete_func_isIE7() ;
}

var autoComplete_bool_isIE = autoComplete_func_isIE();

