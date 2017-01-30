<%@ taglib uri="/WEB-INF/sitemesh-decorator.tld" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/sitemesh-page.tld" prefix="page" %>
<%@page import="id.base.app.webMember.SessionConstants"%>
<%@page import="id.base.app.SystemConstant"%>
<%@page import="id.base.app.webMember.SessionConstants"%>
<%@page import="id.base.app.valueobject.AppFunction"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedList"%>
<%@page import="id.base.app.LoginSession"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html>
<!--<![endif]-->
    <head>
		<meta charset="utf-8"/>
		<title>Base &nbsp;-&nbsp; Admin Application - <decorator:title default="Welcome!" /></title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta charset="UTF-8" />
		<meta content="base application" name="description"/>
		<meta content="base.id team" name="author"/>
    
    	<link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/images/favicon.ico" />
    	
    	<!-- Bootstrap -->
	    <link href="<%=request.getContextPath()%>/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
	    <link href="<%=request.getContextPath()%>/css/vendor/bootstrap/bootstrap-responsive.min.css" rel="stylesheet">
		<link href="<%=request.getContextPath()%>/css/font-awesome.css" rel="stylesheet">
		<link rel="stylesheet" href='<%=request.getContextPath()%>/plugin/vendor/datatables/css/dataTables.bootstrap.css'>
	    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
	    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">
	    
	    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	    <!--[if lt IE 9]>
	      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	    <![endif]-->
	    <script src="<%=request.getContextPath()%>/js/jquery/jquery-2.1.3.min.js" type="text/javascript"></script>
	    <script src="<%=request.getContextPath()%>/js/jquery/jquery.alphanumeric.js" type="text/javascript"></script>
		<script>
		function setMenuActive(menu){
			$('ul.mainnav li.active').removeClass('active');
			$('ul.mainnav li#'+menu).addClass('active');
		}
		
       	$(document).ready(function() {
       		$(document).ajaxStart(function() {
       			startAJAXLoader();
       			
       		}).ajaxStop(function() {
       			stopAJAXLoader();
       			
       		}).ajaxError(function(e, jqxhr, settings, exception) {
       			var ajaxExpired = jqxhr.getResponseHeader('ajax-expired');
       			if(!ajaxExpired || (ajaxExpired && ajaxExpired != 'true')) {
       				alert('System error occurred. Please refresh the browser and contact the system administrator.');	
       			}
       		}).ajaxComplete(function(e, jqxhr, settings) {
       			var ajaxExpired = jqxhr.getResponseHeader('ajax-expired');
       			if(ajaxExpired=='true'){
       				bootBoxError("Session Expired", function(){
       					var loginURL = jqxhr.getResponseHeader('loginURL');
       					if(loginURL) {
       						window.location = loginURL;
       					}
       				});
       			}
       		});
       	});
       	</script>
	</head>
	<!-- Preloader -->
    <div id="mask" class="mask"><div id="loader"></div></div>
    <!--/Preloader -->
	
	<body class="bg-1" onload="initAJAXLoader('<%=request.getContextPath()%>'); initializeDialog(5);">
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse"><span class="icon-bar"></span><span
					class="icon-bar"></span><span class="icon-bar"></span> </a><a
					class="brand" href="index.html">Base Apps </a>
				<div class="nav-collapse">
					<ul class="nav pull-right">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<i class="fa fa-icon-user"></i> <% LoginSession user = (LoginSession) session.getAttribute("user");%><%=user.getName() %> <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a id="logoutBtn" href="#">Logout</a></li>
							</ul>
						</li>
					</ul>
					<script>
		            	$('#logoutBtn').click(function(){
		                	window.location.href = '/Web/do/login/out';
						});
					</script>
				</div>
				<!--/.nav-collapse -->
			</div>
			<!-- /container -->
		</div>
		<!-- /navbar-inner -->
	</div>
	<%
		LinkedList<AppFunction> menus = (LinkedList<AppFunction>) session.getAttribute(SessionConstants.MENU_OBJECT_KEY);
		if (menus != null && menus.size() > 0) {
	%>
	<!-- Wrap all page content here -->
    <div id="wrap">

      <!-- Make page fluid -->
      <div class="row">

			<div class="subnavbar">
				<div class="subnavbar-inner">
					<div class="container">
						<ul class="mainnav">
							<%
						          	for(AppFunction menu : menus) {
						          		if (menu.getFkAppFunctionParent().equals(1L)){
						          			if(AppFunction.isMenuHasChild(menu.getPkAppFunction(), menus)) {
						          				%>
			                 					<li id="<%=menu.getName()%>" class="dropdown">
													<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
														<span><%=menu.getName()%></span>
														<b class="caret"></b>
													</a>
													<ul class="dropdown-menu">
													<%
													for(AppFunction subMenu : menus) {
														if(subMenu.getFkAppFunctionParent() != null && subMenu.getFkAppFunctionParent().equals(menu.getPkAppFunction())) {
															%>
															<li>
																<a href="<%=request.getContextPath() + subMenu.getAccessPage()%>"><%=subMenu.getName()%></a>
						                  					</li>
															<%
														}
													}
													%>
													</ul>
												</li>
				                 				<%
						          			} else {
						          				%>
						          				<li id="<%=menu.getName()%>">
						          					<a href="<%=request.getContextPath() + menu.getAccessPage()%>">
						          						<span><%=menu.getName()%></span>
						          					</a>
						          				</li>
												<%
						          			}
						          		}
						          	}
						          	%>
						</ul>
					</div>
					<!-- /container -->
				</div>
				<!-- /subnavbar-inner -->
			</div>
			<!-- /subnavbar -->

			<!-- Page content -->  
			<div class="main">
				<div class="main-inner">
			    	<div class="container">
	    				<decorator:body />
	    			</div>
	    		</div>
	    	</div>
	    <!-- Page content end -->
	    
	  </div>
      <!-- Make page fluid-->
      
    </div>
    <!-- Wrap all page content end -->

	<section class="videocontent" id="video"></section>

	<%}else{ %>
	   <decorator:body />
       <jsp:include page="/blank.jsp"></jsp:include>
    <%} %>
    <script src="<%=request.getContextPath()%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/jquery/jquery-ui.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/plugin/vendor/datatables/jquery.dataTables.min.js"></script>
    <script src="<%=request.getContextPath()%>/plugin/vendor/bootbox/bootbox.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/modal-dialog.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/common.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery/autoNumeric.js" type="text/javascript"></script>
	
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/plugin/vendor/bootstrap/bootstrap.js"></script>
    
    <!--Start of Tawk.to Script-->
	<script type="text/javascript">
	var Tawk_API=Tawk_API||{}, Tawk_LoadStart=new Date();
	(function(){
	$('.numeric').autoNumeric('init', {aSep:'.', aDec:',', vMin: '-9999999999.99', vMax:'9999999999.99'});
	var s1=document.createElement("script"),s0=document.getElementsByTagName("script")[0];
	s1.async=true;
	s1.src='https://embed.tawk.to/573dfd4cc59e45d972c2fa1e/default';
	s1.charset='UTF-8';
	s1.setAttribute('crossorigin','*');
	s0.parentNode.insertBefore(s1,s0);
	})();
	</script>
	<!--End of Tawk.to Script-->
	</body>
<!-- END BODY -->
</html>
