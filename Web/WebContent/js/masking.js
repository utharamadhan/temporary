// masking.js
// global variable



function MaskMoney(obj,mask){
	var sValue=obj.value;
    if (sValue.length == 0) obj.value= "";
	
	
	var zChar = new Array(' ', ',');
	sValue = ParseChar(sValue, zChar);
	
	if (!mask){
		if (sValue!=""){
			if (parseFloat(sValue)==0){
				obj.value="";
			}else{
				obj.value=parseFloat(sValue);
			}
		}
		return obj.value;
	}

	if (sValue=="") sValue=0; 
	
	var iValue = parseFloat(sValue);
	iValue = (Math.round(iValue * 100)) / 100;
    if (isNaN(iValue)){
		obj.value="";
		return;
	}
	sValue = iValue.toString();
   if (sValue.indexOf(".") == -1) {
       sValue = sValue + ".00";
    }else{
        if (sValue.indexOf(".") == sValue.length - 1){
            sValue = sValue + "00";
        }else if (sValue.indexOf(".") == sValue.length - 2){
            sValue = sValue + "0";
        }
    }
       if (sValue.indexOf(".") > 3){
	 	for (var i=0;i<Math.floor((sValue.indexOf(".")-(1+i))/3);i++){
		    sValue = sValue.substring(0,sValue.indexOf(".") -(4*i+ 3)) + "," + sValue.substring(sValue.indexOf(".") - (4*i+ 3),sValue.length);
		}
    }
	obj.value= sValue;
}

function MaskZip(obj,mask){
    var sNewValue = obj.value;
    var iLength = 5;
    var zChar = new Array(' ', '-');
    sNewValue = ParseChar(sNewValue, zChar);
	if (!mask) {
		obj.value=sNewValue;
		return sNewValue;
	}
	
	if (sNewValue=="") return "";
	 if (isNaN(parseFloat(sNewValue))){
        alert("Not a valid zipcode "+ sNewValue );
     	obj.value="";
		return "";
	}    
   	if (sNewValue.length == 0){
		 obj.value="";
    }else if (sNewValue.length < 5) {
        while (sNewValue.length < 5) sNewValue += "0";
    } else if (sNewValue.length < 9 && sNewValue.length > 5){
        sNewValue = sNewValue.substring(0,5);
        alert("Not a valid Zip Code");
    }else if (sNewValue.length > 9) {
        sNewValue = sNewValue.substring(0,9);
        alert("Not a valid Zip Code");
    }
    
    if (sNewValue.length > 5)
    {
        sNewValue = sNewValue.substring(0,5) + "-" + sNewValue.substring(5,9);
    }
     obj.value=sNewValue;
     return sNewValue;
}




function MaskPhone(obj,mask){
    var sNewValue = obj.value;
    var iLength = 8;
    var zChar = new Array(' ', '(', ')', '-', '.');
	sNewValue = ParseChar(sNewValue, zChar);
	if (!mask){
		obj.value=sNewValue;
		return sNewValue;
	}
	if (sNewValue=="") return "";
 	if (sNewValue.length == 8);
    else if (sNewValue.length == 10) iLength = 10;
    else if (sNewValue.length == 0) obj.value="";
    else if (sNewValue.length < 8)
    {
        while (sNewValue.length < 8) sNewValue += "0";
        alert("Not a valid phone number");
    }
    else if (sNewValue.length < 10)
    {
        sNewValue = sNewValue.substring(0,8);
        alert("Not a valid phone number");
    }
    else if (sNewValue.length > 10)
    {
        iLength = 10;
        
        if (sNewValue.charAt(0) == "1" && sNewValue.length == 11)
        {
            sNewValue = sNewValue.substring(1,11);
        }
        
        else sNewValue = sNewValue.substring(0,10);
        alert("Not a valid phone number");
    }
    sNewValue = FormatPhone(sNewValue,iLength);
    obj.value=sNewValue;
    return sNewValue;
}



function FormatPhone(sPhone, iLength)
{
    var sNewPhone = "";

    if (iLength == 8)
    {
        sNewPhone = sPhone.substring(0,3) + "-" + sPhone.substring(3,8);
    }
    if (iLength == 10)
    {
        sNewPhone = "(" + sPhone.substring(0,3) + ") " + sPhone.substring(3,6)
            + "-" + sPhone.substring(6,10);
    }

    return sNewPhone;
}

function ParseChar(sStr, sChar){
    if (sChar.length == null) {
        zChar = new Array(sChar);
    } else zChar = sChar;
    
     for (i=0; i<zChar.length; i++) {
        sNewStr = "";
    	var iStart = 0;
        var iEnd = sStr.indexOf(sChar[i]);
    
        while (iEnd != -1){
            sNewStr += sStr.substring(iStart, iEnd);
            iStart = iEnd + 1;
            iEnd = sStr.indexOf(sChar[i], iStart);
        }
        sNewStr += sStr.substring(sStr.lastIndexOf(sChar[i]) + 1, sStr.length);
        
        sStr = sNewStr;
    }
    return sNewStr;
}
