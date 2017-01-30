/* ***********************************************************
Example 4-3 (DHTMLAPI.js)
"Dynamic HTML:The Definitive Reference"
2nd Edition
by Danny Goodman
Published by O'Reilly & Associates  ISBN 1-56592-494-0
http://www.oreilly.com
Copyright 2002 Danny Goodman.  All Rights Reserved.
************************************************************ */
// DHTMLapi.js custom API for cross-platform
// object positioning by Danny Goodman (http://www.dannyg.com).
// Release 2.0. Supports NN4, IE, and W3C DOMs.

var isCSS, isW3C, isIE4, isNN4, isIE6CSS;

// Initialize upon load to let all browsers establish content objects
function initDHTMLAPI() {
	if (document.images) {
		isCSS = (document.body && document.body.style) ? true : false;
		isW3C = (isCSS && document.getElementById) ? true : false;
		isIE4 = (isCSS && document.all) ? true : false;
		isNN4 = (document.layers) ? true : false;
		isIE6CSS = (document.compatMode && document.compatMode.indexOf("CSS1") >= 0) ? true : false;
	}
}

// Set event handler to initialize API
window.onload = initDHTMLAPI;

// Convert object name string or object reference
// into a valid style (or NN4 layer) reference
function getObject(obj) {
    var theObj = getRawObject(obj);
    if (theObj && isCSS) {
        theObj = theObj.style;
    }
    return theObj;
}

// Convert object name string or object reference
// into a valid element object reference
function getRawObject(obj) {
    var theObj;
    if (typeof obj == "string") {
        if (isW3C) {
            theObj = document.getElementById(obj);
        } else if (isIE4) {
            theObj = document.all(obj);
        } else if (isNN4) {
            theObj = seekLayer(document, obj);
        }
    } else {
        // pass through object reference
        theObj = obj;
    }
    return theObj;
}

// Retrieve the x coordinate of a positionable object
function getObjectLeft(obj)  {
    var elem = getRawObject(obj);
    var result = 0;
    if (document.defaultView) {
        var style = document.defaultView;
        var cssDecl = style.getComputedStyle(elem, "");
        result = cssDecl.getPropertyValue("left");
    } else if (elem.currentStyle) {
        result = elem.currentStyle.left;
    } else if (elem.style) {
        result = elem.style.left;
    } else if (isNN4) {
        result = elem.left;
    }
    return parseInt(result);
}

// Retrieve the y coordinate of a positionable object
function getObjectTop(obj)  {
    var elem = getRawObject(obj);
    var result = 0;
    if (document.defaultView) {
        var style = document.defaultView;
        var cssDecl = style.getComputedStyle(elem, "");
        result = cssDecl.getPropertyValue("top");
    } else if (elem.currentStyle) {
        result = elem.currentStyle.top;
    } else if (elem.style) {
        result = elem.style.top;
    } else if (isNN4) {
        result = elem.top;
    }
    return parseInt(result);
}

function getElementStyle(elemID, IEStyleAttr, CSSStyleAttr) {
    var elem = document.getElementById(elemID);
    if (elem.currentStyle) {
        return elem.currentStyle[IEStyleAttr];
    } else if (window.getComputedStyle) {
        var compStyle = window.getComputedStyle(elem, "");
        return compStyle.getPropertyValue(CSSStyleAttr);
    }
    return "";
}

/*
 * To get the width of current window
 * 
 * Created by Faber on 16th February 2010
 */
function getWidth() {
	var x = 0;

	if (self.innerHeight) {
		x = self.innerWidth;
	}
	else if (document.documentElement && document.documentElement.clientHeight) {
		x = document.documentElement.clientWidth;
	}
	else if (document.body) {
		x = document.body.clientWidth;
	}

	return x;
}