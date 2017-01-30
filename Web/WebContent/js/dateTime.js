var    monthName =    new    Array("January","February","March","April","May","June","July","August","September","October","November","December")

function getDate2String(tgl,strFormat){
    var d=new Date();
    var xData=    new Array(2);
    var dateFormat=strFormat;
    var formatChar=" ";
    var aFormat    = dateFormat.split(formatChar)
    if (aFormat.length<3){
        formatChar = "/"
        aFormat    = dateFormat.split(formatChar)
        if (aFormat.length<3){
            formatChar = "."
            aFormat    = dateFormat.split(formatChar)
            if (aFormat.length<3){
                formatChar = "-"
                aFormat    = dateFormat.split(formatChar)
                if (aFormat.length<3){
                    formatChar=""
                }
            }
        }
    }
    d=tgl;
    if ( formatChar!= "" ){
        for    (i=0;i<3;i++){
            if ((aFormat[i]=="d") || (aFormat[i]=="dd")){
                xData[i]=d.getDate();
            }else if((aFormat[i]=="m") || (aFormat[i]=="mm")){
                xData[i]=d.getMonth();
            }else if (aFormat[i]=="yyyy"){
                xData[i]=d.getFullYear();
            }else if    (aFormat[i]=="mmm"){
                for    (j=0; j<12;    j++){
                    if (d.getMonth()==j){
                        xData[i]=monthName[j]
                    }
                }
            }

        }

    }
    return (xData[0]+formatChar+xData[1]+ formatChar +xData[2]);
}


function tick() {
  //Clock.innerHTML = getDate2String(today,"dd mmm yyyy")+"  " + timeString ;
  window.status=getDateTime();
  window.setTimeout("tick();", 100);

}
function getTime(){
  var hours, minutes, seconds, ap;
  var intHours, intMinutes, intSeconds;
  var today = new Date();


  var intHours = today.getHours();
  var intMinutes = today.getMinutes();
  var intSeconds = today.getSeconds();

  if (intHours == 0) {
     hours = "12:";
     ap = "Midnight";
  } else if (intHours < 12) {
     hours = intHours+":";
     ap = "A.M.";
  } else if (intHours == 12) {
     hours = "12:";
     ap = "Noon";
  } else {
     intHours = intHours - 12
     hours = intHours + ":";
     ap = "P.M.";
  }

  if (intMinutes < 10) {
     minutes = "0"+intMinutes+":";
  } else {
     minutes = intMinutes+":";
  }

  if (intSeconds < 10) {
     seconds = "0"+intSeconds+" ";
  } else {
     seconds = intSeconds+" ";
  }

  timeString = hours+minutes+seconds+ap;
  return timeString;
}
function getDateTime(){
  return getDate2String(new Date(),"dd mmm yyyy")+"  " + getTime() ;

}

//window.onload = tick;
