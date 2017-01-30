// validator.js

function isNumber(s){
	if(s==null) return false;
	var i;
	for (i = 0; i < s.length; i++){
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

/*
 * validate input for number only
 */
function numberInput(){
   	if (event.keyCode < 48 || event.keyCode > 57 ) {
   		event.returnValue = false;
   	}
}	

function numberInput2(myfield, e, dec) {
	var key;
	var keychar;

	if (window.event){
	   key = window.event.keyCode;
	}else if (e){
	   key = e.which;
	}else{
	   return true;
	}
	keychar = String.fromCharCode(key);

	// control keys
	if ((key==null) || (key==0) || (key==8) || 
	    (key==9) || (key==13) || (key==27) )
	   return true;

	// numbers
	else if ((("0123456789").indexOf(keychar) > -1))
	   return true;

	// decimal point jump
	else if (dec && (keychar == "."))
	   {
	   myfield.form.elements[dec].focus();
	   return false;
	   }
	else{
	   return false;
	}
}

/*
 * validate input for number + '-' only
 */
function numberInputChar(){
   	if (event.keyCode != 45 && event.keyCode < 48 || event.keyCode > 57 ) {
   		event.returnValue = false;
   	}
}

/*
 * validate input for decimal value
 */
function decimalInput(){
    var s = String.fromCharCode(event.keyCode);
   	if ((event.keyCode >= 48 && event.keyCode <= 57) || (s == '.') || (s == '-')) {
   		event.returnValue = true;
   	}else{
   		event.returnValue = false;
   	}
}	

//var REDecimal = new RegExp("^[-]?[0-9]{0,}(([\\.][0-9]{0,})|([\\.]?))?$");
var REDecimal = new RegExp("^(([0-9]{0,})|([-][0-9]{1,}))(([\\.][0-9]{0,})|([\\.]?))?$");
function checkDecimalInput(){
	var s 	= window.event.srcElement.value;
	var ok 	= REDecimal.test(s);
	if(ok == false){
		alert("Invalid format number");
   		event.returnValue = false;
	}
}

var RESecurity = new RegExp("^\w");
function checkSecurityInput(){
	var s 	= window.event.srcElement.value;
	var ok 	= RESecurity.test(s);
	if(ok == false){
		alert("Invalid format. Should be either 0-9 a-z or A-Z.");
   		event.returnValue = false;
	}
}
/*
 * validate input for security value
 */
function securityInput(){
    var s = String.fromCharCode(event.keyCode);
   	if ((event.keyCode >= 48 && event.keyCode <= 57) || 
   	(event.keyCode >= 65 && event.keyCode <= 90) || 
   	(event.keyCode >= 97 && event.keyCode <= 122)) {
   		event.returnValue = true;
   	}else{
   		event.returnValue = false;
   	}
}

/*
 * validate input for security value
 */
function securityInputWithSpecialChar(){
    
   	if ((event.keyCode >= 48 && event.keyCode <= 57) || 
   	(event.keyCode >= 65 && event.keyCode <= 90) || 
   	(event.keyCode >= 97 && event.keyCode <= 122) ||
   	event.keyCode == 95 ) {
   		event.returnValue = true;
   	}else{
   		event.returnValue = false;
   	}
}


//Disable right mouse click Script
//By Maximus (maximus@nsimail.com) w/ mods by DynamicDrive
//For full source code, visit http://www.dynamicdrive.com
var message="Function Disabled!";
///////////////////////////////////
function clickIE4(){
	if (event.button==2){
		alert(message);
		return true;
	}
}

function clickNS4(e){
	if (document.layers||document.getElementById&&!document.all){
		if (e.which==2||e.which==3){
			alert(message);
			return true;
		}
	}
}

if (document.layers){
	document.captureEvents(Event.MOUSEDOWN);
	document.onmousedown=clickNS4;
} else if (document.all&&!document.getElementById){
	document.onmousedown=clickIE4;
}

function isValidTime(){
	
	if (event.keyCode < 48 || event.keyCode > 58) {
   		event.returnValue = false;
   	}
}

//input only 0-9 and can input only one '-' in front
function numericWithSpecChar(obj){
    var sValue=obj.value;    
    if (sValue.length == 0) obj.value= "";

    if (sValue.indexOf("-")==0) {
		var the_char= '';
		var idx = 0;
		for (var i=0 ; i<sValue.length ; i++)
		{
			if (sValue.charAt(i)!="-") 
			{
				idx+=i;
				sValue = sValue.substring(idx);
				
				if (isNaN(sValue))
				{
				 obj.value="";
				}
				else
				{
					obj.value= "-"+sValue;
				}
				return;
			}
			
		}
    }
	else
	{
		if (isNaN(sValue))
		{
			 obj.value="";
			 return;
		}
		for (var i=0 ; i<sValue.length ; i++)
		{
			if (sValue.charAt(i)=="-") 
			{
				 obj.value="";
				 return;
			}
		}
	}
	obj.value= sValue;
}

//document.oncontextmenu=new Function("alert(message);return false")
//document.oncontextmenu=new Function("return false")
