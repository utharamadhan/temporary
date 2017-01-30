var numbersOfModal = 0;

function initializeDialog(numbers){
	if(!numbers){
		numbersOfModal = 1;
		numbers=1;
	}else{
		numbersOfModal = numbers;
	}
	for(var i=1;i<=numbers;i++){
		$('<div id="dialog' + i + '" title="" ></div>').appendTo("body");
		$( "#dialog"+i )
		.dialog({
			autoOpen: false,
			modal: true,
			stack: true,
			beforeClose: function(event, ui) {
				if(event.keyCode == 27) {
					if(typeof closeOnEscape != 'undefined'){
						//alert(jQuery.isFunction(closeOnEscape));
	                    if(jQuery.isFunction(closeOnEscape)){
	                    	closeOnEscape();
	                    }
					}
                }
			},
			close: function(event, ui) {
				if(event.keyCode == 27) {
					$("#dialog"+i).html("");
					if(typeof closeOnEscape != 'undefined'){
						//alert("true");
						closeOnEscape = null;
					}
				}
			},
			open: function(event, ui) {

			}
		});
	}
}

function showScreen(fromId,toId,url){
	if($("#"+fromId).length==0){
		alert("From Transition Page Not Exist");
		return;
	}
	if($("#"+toId).length==0){
		alert("To Transition Page Not Exist");
		return;
	}
	$("#"+fromId).hide();
	$.get(url, function(data){
		if(data=="null"){
			infoDialog("Data has been modified, please re-filter your enquiry", null, null, "Information", closeDialog);
			$("#"+toId).hide();
		    $("#"+fromId).show();
		}else{
			$("#"+toId).show().html(data);			
		}
	}).fail(function() {
		$("#"+toId).hide();
	    $("#"+fromId).show();
	});
}

function returnToList(thisId, listId, isRefresh){
	$("#"+thisId).hide();
	$("#"+thisId).html("");
	$("#"+listId).show();
	if(isRefresh) {
		search();
	}
	$('#content').css('min-height',"");
}

function openDialog(width, height, zIndex, title, data, number){
	var windowHeight = $(window).height();
	if(!number){
		for(var i=1; i<=numbersOfModal; i++){
			if($("#dialog"+i).dialog("isOpen")==false){
				$("#dialog"+i).dialog("option", "zIndex", zIndex);
				$("#dialog"+i).dialog("option", "title", title);
				var browserName=navigator.appName; 
				var browserVer=parseInt(navigator.appVersion); 
				if (browserName=="Microsoft Internet Explorer"){
					$("#dialog"+i).dialog("option", "width",!width?"auto":width+"px");  
					$("#dialog"+i).dialog("option", "height",!height?"auto":height+"px");
				}else {
					$("#dialog"+i).dialog("option", "width", !width?"auto":width);  
					$("#dialog"+i).dialog("option", "height", !height?"auto":height); 
				}
				$("#dialog"+i).html(data).dialog('open');
				if(height!=null){
					if (browserName=="Microsoft Internet Explorer"){
						$("#dialog"+i).height(height).parent().css('top',((windowHeight - height)/2));
						$("#dialog"+i).scrollTop(0);
					}
				}
				break;
			}
		}
	}
	else{
		if(numbersOfModal==0){
			alert("No dialog initialized..");
		}
		else if(number > numbersOfModal){
			alert("There are only " + numbersOfModal + " dialogs initialized..");
		}
		else{
			var browserName=navigator.appName; 
			var browserVer=parseInt(navigator.appVersion); 
			if (browserName=="Microsoft Internet Explorer"){
				$("#dialog"+number).dialog("option", "width",!width?"auto":width+"px");  
				$("#dialog"+number).dialog("option", "height",!height?"auto":height+"px"); 				
			}else {
				$("#dialog"+number).dialog("option", "width",!width?"auto":width);  
				$("#dialog"+number).dialog("option", "height",!height?"auto":height); 
			}
			$("#dialog"+number).dialog("option", "zIndex", zIndex);
			$("#dialog"+number).dialog("option", "title", title);
			$("#dialog"+number).html(data).dialog('open');
			if(height!=null){
				if (browserName=="Microsoft Internet Explorer"){
					$("#dialog"+number).height(height).parent().css('top',((windowHeight - height)/2));
					$("#dialog"+number).scrollTop(0);
				}
			}
		}
	}
}

function closeDialog(number){
	if(!number){
		for(var i=numbersOfModal; i>=1; i--){
			if($("#dialog"+i).dialog("isOpen")){
				// resetting button configuration for new dialog  
				$("#dialog"+i).dialog("option", "buttons", {});
				$("#dialog"+i).dialog('close');
				// to be safe, "dispose" the previous content
				$("#dialog"+i).html("");
				break;
			}
		}
	}else{
		if(numbersOfModal==0){
			alert("No dialog initialized..");
		}
		else if(number > numbersOfModal){
			alert("There are only " + numbersOfModal + " dialogs initialized..");
		}
		else{
			// resetting button configuration for new dialog  
			$("#dialog"+number).dialog("option", "buttons", {});
			$("#dialog"+number).dialog('close');
			// to be safe, "dispose" the previous content
			$("#dialog"+number).html("");
		}
	}
}

function fillDialogContent(data, number){
	if(!number)
		for(i=numbersOfModal; i>=1; i--){
			if($("#dialog"+i).dialog("isOpen")){
				number = i;
				break;
			}
		}
		//number = 1;
	if(numbersOfModal==0){
		alert("No dialog initialized..");
	}
	else if(number > numbersOfModal){
		alert("There are only " + numbersOfModal + " dialogs initialized..");
	}
	else{
		$("#dialog"+number).html(data);
	}
}

function isDialogOpen(number){
	if(!number)
		number = 1;
	if(numbersOfModal==0){
		alert("No dialog initialized..");
	}
	else if(number > numbersOfModal){
		alert("There are only " + numbersOfModal + " dialogs initialized..");
	}
	else{
		return $("#dialog"+number).dialog("isOpen");
	}
}

function initAJAXLoader(base_url){
	if (document.all) {
		document.all.mask.style.display='none';
		document.all.loader.style.display='none';
	}
	if (document.layers) {
		document.mask.style.display='none';
		document.loader.style.display='none';
	}
	if (document.getElementById) {
		document.getElementById('mask').style.display='none';
		document.getElementById('loader').style.display='none';
	}
}

function startAJAXLoader(){
	$("#loader, #mask").css('display', 'block');
}

function stopAJAXLoader(){
	$("#loader, #mask").css('display', 'none');
}

function displayError(data, num){
	if(!num || num==1){
		$("#errorFragmentList").removeClass();
		$("#errorFragmentList").addClass('errorFragmentList');
		$("#errorFragmentList").html(data);
	}
	else{
		$("#errorFragmentList"+num).removeClass();
		$("#errorFragmentList"+num).addClass('errorFragmentList');
		$("#errorFragmentList"+num).html(data);
	}
}

function displayErrorParsley (errors, num) {
	var errorHtml = "";
	for(var i=0;i<errors.length;i++) {
		var error = errors[i];
		if(error.validatedField){
			var field = $('input[name="'+error.validatedField+'"]');
			if (field.length === 0) {
				field = $('select[name="'+error.validatedField+'"]');
			}
			field.addClass('parsley-validated parsley-error');
			field.closest('.form-input-container').append('<label class=\'parsley-error-list\'>'+error.error+'</label>');
		} else {
			errorHtml += "<div class='alert alert-danger'>";
			errorHtml += "<a href='#' class='close' data-dismiss='alert' aria-label='close' title='close' style='font-size:15px;'>&#10006;</a>";
			errorHtml += error.error;
			errorHtml += "</div>";
		}
	}
	
	if (errorHtml != "") {
		displayError(errorHtml, num);
	}
	
	$('.parsley-validated, .parsley-error').click(function(e){
		e.preventDefault();
		$(this).removeClass('parsley-validated parsley-error');
		$(this).closest('.form-input-container').find('.parsley-error-list').remove();
	});
}

function showNotificationSuccess (text, num) {
	var errorHtml = "<div class='alert alert-success'>";
		errorHtml += "<a href='#' class='close' data-dismiss='alert' aria-label='close' title='close' style='font-size:15px;'>&#10006;</a>";
		errorHtml += text;
		errorHtml += "</div>";
	displayError(errorHtml, num);
}

function cleanErrorParsley (num) {
	$('.parsley-validated').removeClass('parsley-validated parsley-error');
	$('.parsley-error-list').remove();
	cleanDisplayErrorJson(num);
}

function displayErrorJson (data, num) {	
	if(data!=undefined){
		var html = "";
		for(var i=0;i<data.length;i++){
			html += "<div class='alert alert-danger'>";
			html += "<a href='#' class='close' data-dismiss='alert' aria-label='close' title='close'>&#10006;</a>";
			html += data[i].error;
			html += "</div>";
		}
		displayError(html,num);
	}
}

function displayErrorJsonSingle (data, num) {
	if(data!=undefined){
		var html = "";
			html += "<div class='alert alert-danger'>";
			html += "<a href='#' class='close' data-dismiss='alert' aria-label='close' title='close'>&#10006;</a>";
			html += data;
			html += "</div>";
		displayError(html,num);
	}
}

function getNumbersOfOpenedDialog(){
	var x = 0;
	for(i=numbersOfModal; i>=1; i--){
		if($("#dialog"+i).dialog("isOpen")){
			x++;
		}
	}
	return x;
}

function cleanDisplayError(){
	var num = getNumbersOfOpenedDialog();
	if(num==0){
		$("#errorFragmentList").removeClass();
		$("#errorFragmentList").html("");
	}
	else{
		$("#errorFragmentList", $("#dialog"+num)).removeClass();
		$("#errorFragmentList", $("#dialog"+num)).html("");
	}
}

function cleanDisplayErrorJson(num){
	if(num==0){
		$("#errorFragmentList").removeClass();
		$("#errorFragmentList").html("");
	}
	else{
		$("#errorFragmentList"+num).removeClass();
		$("#errorFragmentList"+num).html("");
	}
}

function displayError2(data){
	var num = getNumbersOfOpenedDialog();
	if(num==0){
		$("#errorFragmentList").removeClass();
		$("#errorFragmentList").addClass('errorFragmentList');
		$("#errorFragmentList").html(data);
	}
	else{
		$("#errorFragmentList", $("#dialog"+num)).removeClass();
		$("#errorFragmentList", $("#dialog"+num)).addClass('errorFragmentList');
		$("#errorFragmentList", $("#dialog"+num)).html(data);
	}
}

function displayErrorArray(data){	
	if(data!=undefined){
		var html = "<ul>";
		for(var i=0;i<data.length;i++){
			html += "<li>"+data[i]+"</li>";
		}
		html += "</ul>";
		displayError2(html);
	}
}

function closeAllOpenDialog(){
	for(var i=1; i<=numbersOfModal; i++){
		if($("#dialog"+i).dialog("isOpen")==true){
			closeDialog(i);
		}
	}
}

function openConfirmationDialog2(message, width, height, zIndex, title, yesCallback, noCallback, number){
	var windowHeight = $(window).height();
	if(!number){
		for(var i=1; i<=numbersOfModal; i++){
			if($("#dialog"+i).dialog("isOpen")==false){
				$("#dialog"+i).dialog("option", "zIndex", zIndex);
				$("#dialog"+i).dialog("option", "title", title);
				var browserName=navigator.appName; 
				var browserVer=parseInt(navigator.appVersion); 
				if (browserName=="Microsoft Internet Explorer"){
					$("#dialog"+i).dialog("option", "width",!width?"auto":width+"px");  
					$("#dialog"+i).dialog("option", "height",!height?"auto":height+"px"); 
					
				}else {
					$("#dialog"+i).dialog("option", "width", !width?"auto":width);  
					$("#dialog"+i).dialog("option", "height", !height?"auto":height); 
				}
				$("#dialog"+i).html('<p>'+message+'</p>').dialog('open');
				if(height!=null){
					if (browserName=="Microsoft Internet Explorer"){
						$("#dialog"+i).height(height).parent().css('top',((windowHeight - height)/2));
						$("#dialog"+i).scrollTop(0);
					}
				}
				break;
			}
		}
	}
	else{
		if(numbersOfModal==0){
			alert("No dialog initialized..");
		}
		else if(number > numbersOfModal){
			alert("There are only " + numbersOfModal + " dialogs initialized..");
		}
		else{
			var browserName=navigator.appName; 
			var browserVer=parseInt(navigator.appVersion); 
			if (browserName=="Microsoft Internet Explorer"){
				$("#dialog"+number).dialog("option", "width",!width?"auto":width+"px");  
				$("#dialog"+number).dialog("option", "height",!height?"auto":height+"px"); 				
			}else {
				$("#dialog"+number).dialog("option", "width",!width?"auto":width);  
				$("#dialog"+number).dialog("option", "height",!height?"auto":height); 
			}
			$("#dialog"+number).dialog("option", "zIndex", zIndex);
			$("#dialog"+number).dialog("option", "title", title);
      				$("#dialog"+number).dialog("option", "buttons", {
					"Yes": function() {
						yesCallback();
						$( this ).dialog( "close" );
						$( this ).dialog( "option", "zIndex", zIndex );
						$( this ).dialog( "option", "buttons", {} );
					},
					"No": function() {
						noCallback();
						$( this ).dialog( "close" );
						$( this ).dialog( "option", "zIndex", zIndex );
						$( this ).dialog( "option", "buttons", {} );
					}
				});
				$("#dialog"+number).html('<p>'+message+'</p>').dialog('open');
			if(height!=null){
				if (browserName=="Microsoft Internet Explorer"){
					$("#dialog"+number).height(height).parent().css('top',((windowHeight - height)/2));
					$("#dialog"+number).scrollTop(0);
				}
			}
		}
	}
}

function openConfirmationDialog(message, width, height, zIndex, title, functionCallback, number, isNotiv){
	var windowHeight = $(window).height();
	if(!number){
		for(var i=1; i<=numbersOfModal; i++){
			if($("#dialog"+i).dialog("isOpen")==false){
				$("#dialog"+i).dialog("option", "zIndex", zIndex);
				$("#dialog"+i).dialog("option", "title", title);
				var browserName=navigator.appName; 
				var browserVer=parseInt(navigator.appVersion); 
				if (browserName=="Microsoft Internet Explorer"){
					$("#dialog"+i).dialog("option", "width",!width?"auto":width+"px");  
					$("#dialog"+i).dialog("option", "height",!height?"auto":height+"px"); 
					
				}else {
					$("#dialog"+i).dialog("option", "width", !width?"auto":width);  
					$("#dialog"+i).dialog("option", "height", !height?"auto":height); 
				}
				$("#dialog"+i).html('<p>'+message+'</p>').dialog('open');
				if(height!=null){
					if (browserName=="Microsoft Internet Explorer"){
						$("#dialog"+i).height(height).parent().css('top',((windowHeight - height)/2));
						$("#dialog"+i).scrollTop(0);
					}
				}
				break;
			}
		}
	}
	else{
		if(numbersOfModal==0){
			alert("No dialog initialized..");
		}
		else if(number > numbersOfModal){
			alert("There are only " + numbersOfModal + " dialogs initialized..");
		}
		else{
			var browserName=navigator.appName; 
			var browserVer=parseInt(navigator.appVersion); 
			if (browserName=="Microsoft Internet Explorer"){
				$("#dialog"+number).dialog("option", "width",!width?"auto":width+"px");  
				$("#dialog"+number).dialog("option", "height",!height?"auto":height+"px"); 				
			}else {
				$("#dialog"+number).dialog("option", "width",!width?"auto":width);  
				$("#dialog"+number).dialog("option", "height",!height?"auto":height); 
			}
			$("#dialog"+number).dialog("option", "zIndex", zIndex);
			$("#dialog"+number).dialog("option", "title", title);
			if(typeof isNotiv != 'undefined'){
				if(isNotiv){
	      			$("#dialog"+number).dialog("option", "buttons", {
						"Ok": function() {
							if(typeof functionCallback != 'undefined'){
								functionCallback();
							};
							closeDialog(number);
						}
					});					
				}
			}else{
      			$("#dialog"+number).dialog("option", "buttons", {
					"Yes": function() {
						if(typeof functionCallback != 'undefined'){
							functionCallback();
						};
						closeDialog(number);
					},
					"No": function() {
						closeDialog(number);
					}
				});				
			}
			$("#dialog"+number).html('<p>'+message+'</p>').dialog('open');
			if(height!=null){
				if (browserName=="Microsoft Internet Explorer"){
					$("#dialog"+number).height(height).parent().css('top',((windowHeight - height)/2));
					$("#dialog"+number).scrollTop(0);
				}
			}
		}
	}
}

function infoDialog(message, width, height, title, functionCallback){
	var number = getNumbersOfOpenedDialog();
	openConfirmationDialog(message, width, height, 1, title, functionCallback, number+1, true);
};

function confirmationDialog(message, width, height, title, functionCallback){
	var number = getNumbersOfOpenedDialog();
	openConfirmationDialog(message, width, height, 1, title, functionCallback, number+1);
};

function removeTextEditor(contentAreaID, parentID, isClass) {
	if (typeof tinyMCE != 'undefined') {
		if(isClass) {
			$('.'+contentAreaID).each(function(){
				tinyMCE.execCommand('mceFocus', false, $(this).attr('id'));
			    tinyMCE.execCommand('mceRemoveEditor', false, $(this).attr('id'));
			    $(this).parent().remove();	
			});
		} else {
			tinyMCE.execCommand('mceFocus', false, contentAreaID);
		    tinyMCE.execCommand('mceRemoveEditor', false, contentAreaID);
		    $(parentID).remove();	
		}
	}
}