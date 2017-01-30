function _openPicName(_obj){
	var obj= document.getElementById(_obj)
	if(obj.style.display==''){
		obj.style.display='none';
	}else{
		obj.style.display='';
	}	
}

function showProductDetail(context,gdnSKU){
	var url = context + "/Product/Product.do?method=showDetail&gdnSkuId="+gdnSKU;
	$.get(url, function(data){
		openDialog(475, 550, 10, "Detil Produk", data, 1);
	});
}

function IsNumeric(input)
{
   return (input - 0) == input && input.length > 0;
}

function sumAllTotal(unitWeight, readOnly, isPickupWait){
	var total=0;
	var totalWeight=0;
	var lblTotal=0;
	var totalLabel=0;
	var idPrivate=idx;
	if(unitWeight!=-1){
		var lblTotal=unitWeight;
	}
	if(readOnly){
		$("#tableKoli tbody tr.koliDetail").each(function(){
			tr=$(this);
			txtBox=tr.find('input:text');
			var name=txtBox.attr("name").replace("kolis","");
			var hidval = document.getElementById("hiddenKolis"+name).value;
			if(IsNumeric(hidval)){
				total=total+parseInt(hidval);
				totalLabel=Math.ceil(txtBox.val()*lblTotal);
				$("#kolisWeight"+name).attr('innerHTML',totalLabel);
				totalWeight=totalWeight+totalLabel;
			}
			idPrivate++;
	  	});
	}else{
		if(!isPickupWait){
			$("#tableKoli tbody tr.koliDetail").each(function(){
				tr=$(this);
				txtBox=tr.find('input:text');
				var name=txtBox.attr("name").replace("kolis","");
				if(IsNumeric(txtBox.val())){
					total=total+parseInt(txtBox.val());
					totalLabel=Math.ceil(txtBox.val()*lblTotal);
					$("#kolisWeight"+name).attr('innerHTML',totalLabel);
					totalWeight=totalWeight+totalLabel;
				}
				idPrivate++;
		  	});
		}else{
			$("#tableKoli tbody tr.koliDetail").each(function(){
				tr=$(this);
				txtBox=tr.find('input:text');
				var name=txtBox.attr("name").replace("kolis","");
				var hidval = document.getElementById("hiddenKolis"+name).value;
				if(IsNumeric(hidval)){
					total=total+parseInt(hidval);
					totalLabel=Math.ceil(txtBox.val()*lblTotal);
					$("#kolisWeight"+name).attr('innerHTML',totalLabel);
					totalWeight=totalWeight+totalLabel;
				}
				idPrivate++;
		  	});
		}
	}
	$("#idTotalProduct").attr('innerHTML',total );
	$("#idTotalWeight").attr('innerHTML',totalWeight );
}

function addKoliRow(unitWeight){
	idx++;
	$("#tableKoli").append("<tr class='koliDetail' ><td  align='center' width='20px'><input type='checkbox' name='koliRowId' id='koliRowId'/></td><td align='center' width='50%'><input type='textbox' name='kolis"+idx+"' id='kolis' maxlength='2' onchange='preSumTotal()' size='2'></td><td align='center' width='50%'><label id='kolisWeight"+idx+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+unitWeight+"</label></td></tr>");
}

function removeKoliRow(){
		$("#tableKoli tbody tr.koliDetail ").each(function(){
		tr=$(this);
		chk=tr.find('input:checkbox');
		if(chk.attr('checked')==true){
			tr.remove();
		}
		});
		preSumTotal();
}

function selectAllKolis(obj){
	$("#tableKoli tbody tr.koliDetail").each(function(){
		tr=$(this);
		chkBox=tr.find('input:checkbox');
		if(obj.checked==true){
			chkBox.attr("checked",true);
		}else{
			chkBox.attr("checked",false);
		}
  	});
}

/*
/ This method used to check Total Product Completed,
/ return 1 if completetotalProduct = maxTotalproduct,
/ return 0 if completetotalProduct between 0 and maxtotalProduct (incomplete product for all request(order))
/ return -1 if complete totalProduct less than 0 
/ return -2 if complete totalProduct is higher than maxTotalProduct
*/
function validateCompleteTotalProduct(inputTotalProduct,systemTotalProduct){
	var completeTotalProduct=inputTotalProduct;
	var maxTotalProduct=systemTotalProduct;
	if(completeTotalProduct!=''){
		if(!isNaN(completeTotalProduct)){
			if(completeTotalProduct < 0){
				return -1;
			}
			if(completeTotalProduct > maxTotalProduct){
				return -2;
			}
			if(completeTotalProduct==maxTotalProduct){
				return 1;
			}
			if(completeTotalProduct<maxTotalProduct){
				return 0;
			}
		}
	}
	return -1;
}

function batchTimeDisabled(bool){
	$("#batchTimePlace").find("input:radio").each(function(){
		var radio=$(this);
		radio.attr("disabled",bool);		
	})
}

function allKoliDisabled(bool){
	$("#btnAddKoli").attr("disabled",bool);
	$("#btnRemoveKoli").attr("disabled",bool);
}

function allBopisDisabled(bool){
	$("#masterCheckBox").attr("disabled",bool);
	$("#tableKoli tbody tr.koliDetail").each(function(){
		tr=$(this);
		chkBox=tr.find("input:checkbox");
		chkBox.attr("disabled",bool);
		txtBox=tr.find('input:text');
		txtBox.attr("disabled",bool);
  	});
}

function validateTotalKoli(unitWeight, maxKoliWeight){
	var bool=true;
	$("#tableKoli tbody tr.koliDetail").each(function(){
		var tr=$(this);
		txtBox=tr.find('input:text');
		txt=txtBox.attr('value');
		if(!isNaN(txt)){
			if((txt*unitWeight) < 0){
				bool=false;
			}
			if((txt*unitWeight) > maxKoliWeight){
				bool=false;
			}
		}
	})
	return bool;
}

function validateTotalWeight(){
	var total=0;
	var totalWeight=$("#completeTotalProduct").val();
	$("#tableKoli tbody tr.koliDetail").each(function(){
		tr=$(this);
		txtBox=tr.find('input:text');
		total=total+parseInt(txtBox.val());
  	});
  	if(total!=totalWeight){
		return false;
  	}
  	return true;
}

function getKolis(){
	var koliUrl=""
	var seq=0;
	$("#tableKoli tbody tr.koliDetail").each(function(){
		tr=$(this);
		txtBox=tr.find('input:text');
		txt=txtBox.attr('value');
		if(jQuery.trim(txt)!=''){
			seq++;
			koliUrl=koliUrl+"&koli="+txt+"&airways=&sequence="+seq;
		}
  	});
  	return koliUrl;
}

function getKolis2(){
	var koliUrl=""
	$("#tableKoli tbody tr.koliDetail").each(function(){
		tr=$(this);
		tr.find('input:text').each(function(){
		txt=$(this);
			if(txt.attr('id')=='kolis'){
				koliUrl=koliUrl+'&koli='+txt.val();
			}
			if(txt.attr('id')=='airway'){
				koliUrl=koliUrl+'&airways='+txt.val();
			}
		});
		tr.find('input:hidden').each(function(){
			hid=$(this);
			if(hid.attr('id')=='sequence'){
				koliUrl=koliUrl+'&sequence='+hid.val();
			}
		});
  	});
  	return koliUrl;
}