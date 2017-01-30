/*
*	contains all of the methods who participate in the scroll process. 
*	there are some variables you can change to adjust the
*	scroll appearance, they are <code>scrollSpeed</code>, and
*	<code>defaultMenuWidth</code>.
*	some of methods defined here were assisted by the other 
*	methods which reside in ifs-sliding-util.js.
*	
*	TODO: get those configurations above out of this file. put 
*	them to another place which is more appropriate to hold 
*	configurations.
*
*	created on Friday, 12nd Feb 2010
*	author Faber
*/

var scrollEngaged = false;
var scrollInterval;
var scrollBars = new Array();

/*
	the delay time to invoke scrollBy function.
	lower values means faster scrolling, and vice versa.
*/
var scrollSpeed = 100;

/*
	this defines how many pixels the scroll
	will take. currently we consider it as the length of 
	each single menu.
*/
var defaultMenuWidth = 80;

/*
	to setup the scroll environment.
	there are two container of the menu to scroll. one of them
	resides within another.
	
	the structure of the menu from the outmost to the in-most is:
	<div>
		<div id=leftID></div>
		<div id=ownerID>
			<div id=ownerContentID> -->menus here<-- </div>
		</div>
		<div id=rightID></div>
	</div>
*/
function scrollBar(ownerID, ownerContentID, leftID, rightID) {
    this.ownerID = ownerID;
    this.ownerContentID = ownerContentID;
    this.index = scrollBars.length;
    this.leftButton = document.getElementById(leftID);
    this.rightButton = document.getElementById(rightID);
    this.leftButton.index = this.index;
    this.rightButton.index = this.index;
	echo(0, this.rightButton.index);
    this.ownerWidth = parseInt(getElementStyle(this.ownerID, "width", "width"));
    this.contentElem = document.getElementById(ownerContentID);
	this.scrollAmount = parseInt(defaultMenuWidth);
    this.contentScrollWidth = (this.contentElem.scrollWidth) ? 
        this.contentElem.scrollWidth : this.contentElem.offsetWidth;
    this.initScroll = initScroll;
}

/*
	to handle the real scroll process.
	this function calculate how many pixels the scroll can take,
	check whether the scroll is engaged or not and take action 
	appropriate for each respective condition.
*/
function scrollBy(index, px) {
    var scroller = scrollBars[index];
    var elem = document.getElementById(scroller.ownerContentID);
    var left = parseInt(elem.style.left);
    var scrollWidth = parseInt(scroller.contentScrollWidth);
    var width = scroller.ownerWidth;
    var navigationWidth = 20;
	var menuLogoutWidth = 50;
	var paddingLeft = 5;
	//Here, we check whether there is more menu to scroll
    if (scrollEngaged && left + px >= -(scrollWidth+menuLogoutWidth+navigationWidth+paddingLeft) + width && left + px <= 0) {
		echo(0, "left: " + left + ", px: " + px + ", scrollWidth: " + scrollWidth + ", width: " + width);
        shiftBy(elem, px, 0);
    } else {
    	echo(0, "left: " + left + ", px: " + px + ", scrollWidth: " + scrollWidth + ", width: " + width);
    	disableNavigation(index, px);
    }
}

/*
	to change the image of a button which denotes
	that it is disabled. actually the disable mode is 
	handled by <code>scrollBy</code> function, but 
	changing the image of the button will make it more clear.

	TODO: add mechanism to re-enable the disabled button when the opposite
	button has been clicked.
*/
function disableNavigation(index,px) {
	var rightArrow = scrollBars[index].rightButton;
	var leftArrow = scrollBars[index].leftButton;
	var scroller = scrollBars[index];
    var elem = document.getElementById(scroller.ownerContentID);
    var x=getAbsX(elem);
    var divWidth=elem.offsetWidth;
    var windowWidth=getWidth();
    var navigationWidth = 20;
	var menuLogoutWidth = 50;
	var paddingLeft = 5;
	var rightPos=x+divWidth;
	if(px <= 0) {
    	if(rightPos<=windowWidth-(navigationWidth+10)){
    		rightArrow.style.visibility="hidden";
    		clearInterval(scrollInterval);
    	}
		if(x<=(navigationWidth+paddingLeft)){
			leftArrow.style.visibility="visible";
			clearInterval(scrollInterval);
		}
	}
	else {
		if(x>=(navigationWidth+paddingLeft)){
			leftArrow.style.visibility="hidden";
			clearInterval(scrollInterval);
		}
		if(rightPos>=windowWidth-(menuLogoutWidth+navigationWidth)){
    		rightArrow.style.visibility="visible";
    		clearInterval(scrollInterval);
    	}
//		var y=(x*-1);
//		if(y>= divWidth-windowWidth){
//			rightArrow.style.visibility="visible";
//		}
	}
}

/* 
	move an object by x and/or y pixels.
	this function is invoked continuously until
	there is no more menu to scroll. this is accomplished
	by setting the left and top value of the target.
*/
function shiftBy(obj, deltaX, deltaY) {
    var theObj = getObject(obj);
    if (theObj) {
        if (isCSS) {
            // equalize the incorrect numeric value type
            var units = (typeof theObj.left == "string") ? "px" : 0; 
            theObj.left = getObjectLeft(obj) + deltaX + units;
            theObj.top = getObjectTop(obj) + deltaY + units;
        } else if (isNN4) {
            theObj.moveBy(deltaX, deltaY);
        }
    }
}

/*	
	the event handler if left and right arrow click event.
	if the event is left-arrow-click, then the scrollAmount
	is positive, and vice versa. 
	note that this function will register the scrollBy
	function with a value so that the method will be invoked
	continuously until some condition make it stops.
*/
function handleScrollClick(evt) {
    var scrollSize;
    evt = (evt) ? evt : event;
    var target = (evt.target) ? evt.target : evt.srcElement;
    var index = target.index;
    scrollSize = scrollBars[index].scrollAmount;
    scrollSize = (target.className == "prevHorizontal") ? scrollSize : -scrollSize;
    scrollEngaged = true;
    scrollBy(index, parseInt(scrollSize));
    scrollInterval = setInterval("scrollBy(" + index + ", " + 
        parseInt(scrollSize) + ")", scrollSpeed);
    
    return false;
}

/*	
	build the scroll, in the other words, the left and right arrow.
	here we set the event handler for each of the button,
	the onmousedown and onmouseup events.
*/
function initScroll() {
    this.leftButton.onmousedown = handleScrollClick;
    this.leftButton.onmouseup = handleScrollStop;

    this.rightButton.onmousedown = handleScrollClick;
    this.rightButton.onmouseup = handleScrollStop;
    disableNavigation(0,0);
    disableNavigation(0,1);
    var isIEMac = (navigator.appName.indexOf("Explorer") != -1 && navigator.userAgent.indexOf("Mac") != -1);
    if (!isIEMac) {
        document.getElementById("theInnerContainer").style.overflow = "hidden";
    }
}

/*
	to handle the onmouseup event.
	this is accomplished simply by setting the scrollEngaged to be false,
	which means the scroll stops.
*/
function handleScrollStop() {
    scrollEngaged = false;
}

function resizeInnerContainer(numOfMenu) {
	var innerContainer = document.getElementById("theInnerContainer");
	innerContainer.width = numOfMenu * defaultMenuWidth;
	innerContainer.contentScrollWidth = numOfMenu * defaultMenuWidth;
}

/*
	to display anything.
	the <code>enable</code> argument is useful to determine
	whether the message must be displayed or not.
	e.g. <code>echo(1, "hey dude!")</code> will display the message,
	and change it to <code>echo(0, "hey dude!")</code> to
	disable. 
*/
function echo(enable, msg) {
	if(enable) alert(msg);
}


function getAbsX(elt) { return (elt.x) ? elt.x : getAbsPos(elt,"Left");}
function getAbsY(elt) { return (elt.y) ? elt.y : getAbsPos(elt,"Top");}

function getAbsPos(elt,which) {
	iPos = 0;
	while (elt != null) {
	iPos += elt["offset" + which];
	elt = elt.offsetParent;
	}
	return iPos;
}

function changeColor(id,menuSize){
	var menuId=id;
	for(i=0;i<menuSize;i++){
		var tempId="id"+i;
		var obj=document.getElementById(tempId);
		if(menuId==tempId){
			obj.style.color='white';			
			obj.style.background='#010B76';
		}else{
			obj.style.color='BLACK';
			obj.style.background='#3CC0EC';
		}
	}
}