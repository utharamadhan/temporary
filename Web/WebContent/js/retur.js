function showProductDetail(context,gdnSKU){
	var url = context + "/Product/Product.do?method=showDetail&gdnSkuId="+gdnSKU;
	$.get(url, function(data){
		openDialog(475, 550, 10, "Detil Produk", data, 1);
	});
}

function sumAllTotal(unitWeight){
	var total=0;
	var totalWeight=0;
	calculateSubTotalWeight(unitWeight);
	$("#tableKoli tr.koliDetail").each(function(){
		tr=$(this);
		txtBox=tr.find('input:text');
		total=total+parseInt(txtBox.val());
		var name=txtBox.attr("id");
		totalWeight=totalWeight+parseInt($("#kolisWeight"+name).attr('innerHTML'));
  	});
  	$("#idTotalProduct").attr('innerHTML',total );
	$("#idTotalWeight").attr('innerHTML',totalWeight );
	
}

function calculateSubTotalWeight(unitWeight){
	$("#tableKoli tr.koliDetail").each(function(){
		tr=$(this);
		txtBox=tr.find('input:text');
		var x = parseInt(txtBox.val());
		totalWeight=x*unitWeight;
		$("#kolisWeight"+txtBox.attr('id')).attr('innerHTML',totalWeight);
  	});
}

function selectAllKolis(obj){
	$("#tableKoli tr.koliDetail").each(function(){
		tr=$(this);
		chkBox=tr.find('input:checkbox');
		if(obj.checked==true){
			chkBox.attr("checked",true);
		}else{
			chkBox.attr("checked",false);
		}
  	});
}

function addKoliRow(unitWeight){
	idx++;
	$("#tableKoli").append("<tr class='koliDetail'><td width='20px' class='listItem2' align='center'><input type='checkbox' name='koliRowId' id='koliRowId'/></td><td  width='50%' class='listItem2' align='center'><input type='text' name='kolis' id='"+idx+"' onChange='sumAllTotal("+unitWeight+");' maxlength='2' size='2' value='1'></td><td  width='50%' class='listItem2' align='center'  style='padding-left: 35px;'><label id='kolisWeight"+idx+"'>"+unitWeight+"</label></td></tr>");
	sumAllTotal(unitWeight);
}

function removeKoliRow(unitWeight){
  	$("#tableKoli tr.koliDetail ").each(function(){
		tr=$(this);
		chk=tr.find('input:checkbox');
		if(chk.attr('checked')==true){
			tr.remove();
			sumAllTotal(unitWeight);
		}
  	});
}

function getKolis(){
	var koliUrl=""
	$("#tableKoli tr.koliDetail").each(function(){
		tr=$(this);
		txtBox=tr.find('input:text');
		txt=txtBox.attr('value');
		if(jQuery.trim(txt)!=''){
			koliUrl=koliUrl+"&koli="+txt;
		}
  	});
  	return koliUrl;
}

function getKolis2(){
	var koliUrl="";
	var seq = 0;
	$("#tableKoli tr.koliDetail").each(function(){
		tr=$(this);
		txtBox=tr.find('input:text');
		koliUrl=koliUrl + '&koliQuantity=' + $("#"+txtBox.attr('id')).val();
		koliUrl=koliUrl + '&koliSubTotal=' + $("#kolisWeight"+txtBox.attr('id')).attr('innerHTML');
		seq++;
		koliUrl=koliUrl + '&koliSequence=' + seq;
  	});
  	return koliUrl;
}

function checkPickupTime(){
	var batchIdx;
	for(var i=0;i<3;i++){
		batchIdx = "#batchTime" + i;
		if($(batchIdx).val()){
			if($(batchIdx).attr("checked")){
				return true;
			}
		}
	}
	alert("Pickup Time harus dipilih.");
	return false;
}