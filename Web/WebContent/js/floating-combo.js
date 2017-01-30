
function setValue(combo, text, save, hide) {
	  if (combo.value == "") {  
	    combo.selectedIndex = 0;
	    text.value = "";
	    save.innerHTML = "";
	    return false;
	  }
	  var description = new String();
	  index = combo.selectedIndex;
	  if (index > 0) {
	     description = combo.options[index].innerHTML;
	     text.value = description.substring(0, description.indexOf('-'));
	     save.innerHTML = description.substring(description.indexOf('-')+1, description.length);
	  }
	  
	  //Upd By Yankim 06/06/2006
	  if(hide && (document.activeElement.name != combo.name) && index > 0){
	     combo.style.display = "none"; 
  	  }
	 return true;
	  
}


function setValueCmb(combo, text, save) {
	  if (combo.value == "") {  
	     combo.selectedIndex = 0;
	    text.value = "";
	    save.innerHTML = "";
	    return false;
	  }
	  
	  var description = new String();
	  index = combo.selectedIndex;
	  if (index > 0) {
	     description = combo.options[index].innerHTML;
	     text.value = description.substring(0, description.indexOf('-'));
	     save.innerHTML = description.substring(description.indexOf('-')+1, description.length);
	  }
     combo.style.display = "none"; 
 	 return true;
	  
}

function showCombo(objName, cmbName) {
	 var p;
    obj = document.getElementById(objName);
    combo = document.getElementById(cmbName);
    if (!obj) return false;
    if (!combo) return false;
    p = getXY(obj);
    combo.style.left = p[0];
    combo.style.top = p[1]+18;
    combo.style.display = "";
    setCombo(obj, combo);
}



function setCombo(obj, cmb) {
    var objValue = obj.value;
    for (i = 0; i < cmb.options.length; i++) {
        optionText = cmb.options[i].text;
         if (objValue.toUpperCase() == optionText.substring(0, objValue.length)) {
            cmb.selectedIndex = i;
            break;
        }
    }
}



function getXY(a,offset) {
    var p=[0,0],tn;
    while(a) {
        tn=a.tagName.toUpperCase();
        p[0]+=a.offsetLeft-(tn=="DIV"&&a.scrollLeft?a.scrollLeft:0);
        p[1]+=a.offsetTop-(!tn=="DIV"&&a.scrollTop?a.scrollTop:0);
        if (tn=="BODY") break;
        a=a.offsetParent;
    }
    return p;
}


/*


function setValue(combo, text, save, hide) {
	  if (combo.value == "") {  
	   // combo.style.display = "none";
	    combo.selectedIndex = 0;
	    text.value = "";
	    save.innerHTML = "";
	    return false;
	  }
	  var description = new String();
	  index = combo.selectedIndex;
	  if (index > 0) {
	     description = combo.options[index].innerHTML;
	     text.value = description.substring(0, description.indexOf('-'));
	     save.innerHTML = description.substring(description.indexOf('-')+1, description.length);
	  }
	  //Upd By Yankim 06/06/2006
	  //if (hide && (document.activeElement.name != undefined && document.activeElement.name != combo.name)) {
	   if(hide || (document.activeElement.name != combo.name)){
			   combo.style.display = "none";    
	  }
	 
	  return true;
}




function showCombo(objName, cmbName) {
	 var p;
    obj = document.getElementById(objName);
    combo = document.getElementById(cmbName);
    if (!obj) return false;
    if (!combo) return false;
    p = getXY(obj);
    combo.style.left = p[0];
    combo.style.top = p[1]+18;
    combo.style.display = "";
    setCombo(obj, combo);
}

function setValueCmb(combo, text, save) {
	  if (combo.value == "") {  
	     combo.selectedIndex = 0;
	    text.value = "";
	    save.innerHTML = "";
	    return false;
	  }
	  
	  var description = new String();
	  index = combo.selectedIndex;
	  if (index > 0) {
	     description = combo.options[index].innerHTML;
	     text.value = description.substring(0, description.indexOf('-'));
	     save.innerHTML = description.substring(description.indexOf('-')+1, description.length);
	  }
	  combo.style.display = "none"; 
	 return true;
	  
}

function setCombo(obj, cmb) {
    var objValue = obj.value;
    for (i = 0; i < cmb.options.length; i++) {
        optionText = cmb.options[i].text;
         if (objValue.toUpperCase() == optionText.substring(0, objValue.length)) {
            cmb.selectedIndex = i;
            break;
        }
    }
}



function getXY(a,offset) {
    var p=[0,0],tn;
    while(a) {
        tn=a.tagName.toUpperCase();
        p[0]+=a.offsetLeft-(tn=="DIV"&&a.scrollLeft?a.scrollLeft:0);
        p[1]+=a.offsetTop-(!tn=="DIV"&&a.scrollTop?a.scrollTop:0);
        if (tn=="BODY") break;
        a=a.offsetParent;
    }
    return p;
}
*/