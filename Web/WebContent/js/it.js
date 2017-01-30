
function get_cookie(Name) {
	var search = Name + "="
	var returnvalue = "";
	if (document.cookie.length > 0) {
		offset = document.cookie.indexOf(search)
		// if cookie exists
		if (offset != -1) {
			offset += search.length
			// set index of beginning of value
			end = document.cookie.indexOf(";", offset);
			// set index of end of cookie value
			if (end == -1) end = document.cookie.length;
			returnvalue=unescape(document.cookie.substring(offset, end))
		}
	}
	return returnvalue;
}

function _detailMerchant(sURL,sTitle){
	$.get(sURL, function(data){
		openDialog(730, 600, 3999, sTitle, data);
	});	
}

function _openDialog(sURL,sTitle,width,height){
	$.get(sURL, function(data){
		openDialog(width, height, 3999, sTitle, data);
	});	
}
function _openDialog(sURL,sTitle,width,height,intX){
	$.get(sURL, function(data){
		openDialog(width, height, 3999, sTitle, data,intX);
	});	
}
