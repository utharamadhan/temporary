<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>Base - Admin Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/images/favicon.ico" />
  	<link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap-responsive.min.css">
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/font-awesome.css">
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/signin.css">
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
			</a> <a class="brand" href="#"> Point System </a>
		</div>
	</div>
</div>
<div class="errorFragment"></div>
<div class="account-container">
	<div class="content clearfix">
		<form action="#" method="post">
			<h1>Admin Login</h1>
				<div class="login-fields">
					<p>Please provide your details</p>
					<div class="field">
						<label for="username">Username</label>
						<input type="text" id="username" name="username" value="" placeholder="Username" class="login username-field" />
					</div>
					<div class="field">
						<label for="password">Password:</label>
						<input type="password" id="password" name="password" value="" placeholder="Password" class="login password-field" />
					</div>
				</div>
				<div class="login-actions">
					<span class="login-checkbox"> 
						<input id="Field" name="Field" type="checkbox" class="field login-checkbox" value="First Choice" tabindex="4" />
						<label class="choice" for="Field">Keep me signed in</label>
					</span>
					<button class="button btn btn-success btn-large btn-login">Sign In</button>
				</div>
			</form>
		</div>
	</div>
	<script src="<%=request.getContextPath()%>/script/jquery-1.7.2.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/script/bootstrap.js" type="text/javascript"></script>
  <script type="text/javascript">
    	$(function(){
	    	<%
	          	if(request.getParameter("error") != null) {
	          		if(request.getParameter("error").equals("wrongAccount")) {
	          			%> 
	          				var alertContent = "<div class='alert'>" +
	          										"<button type='button' class='close' data-dismiss='alert'>×</button>" +
													"<strong>invalid username / password.</strong>" +
												"</div>";
	            			$(".errorFragment").append(alertContent);
	            		<%	
	          		} else if(request.getParameter("error").equals("tokenExpired")) {
	          			%> 
	          				var alertContent = "<div class='alert'>" +
													"<button type='button' class='close' data-dismiss='alert'>×</button>" +
													"<strong>token expired.</strong>" +
												"</div>";
							$(".errorFragment").append(alertContent);
            			<%	
          			}
	          	}
	        %>
    		
    		
    		//BEGIN: change this to your server environment
    		var protocol = 'http';
    		var domain = '<%=request.getServerName()%>';
    		var defaultPort = '<%=request.getServerPort()%>'; 
    		var webAdminContext = '/WebAdmin';
    		var webTransContext = '/Web';
    		var minutes = 20; //don't set more than 60
    		//END: change this to your server environment
    		
    		var uniqueToken = 'sSs';
    		
    		Date.prototype.yyyymmdd = function() {
    		   var yyyy = this.getFullYear().toString();
    		   var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
    		   var dd  = this.getDate().toString();
    		   var hr = this.getHours().toString();
    		   var min = this.getMinutes().toString();
    		   return yyyy + (mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]) + (hr[1]?hr:"0"+hr[0]) + (min[1]?min:"0"+min[0]); // padding
    		  };
    		
    		var checkUserName = function(user, password){
    			if(user==''){
    				alert('Please insert user name');
    				return false;
    			}else if(password=''){
    				alert('Please insert your password');
    				return false;
    			}
    			return true;
    		}
    		
    		var loginPost = function(num){
    			var user = document.getElementById('username').value;
    			var password = document.getElementById('password').value;
    			if(checkUserName(user, password)){
    				var currToken = uniqueToken;
    				var currPort = defaultPort;
    				var host = domain;
    				var ptcl = protocol;
    				var restServiceURL = '/do/landingPage';
    				var token = '900|'+user+'|'+getExpiry()+'|'+password+'|sadasdaijwiajdiwdakdmaskdmasdksaa'+currToken;
    				var url = ptcl+"://"+host+":"+currPort;
    				if(num==1){	
    					url = url + webAdminContext + restServiceURL;
    				}else if(num==2){
    					url = url + webTransContext + restServiceURL;
    				}
    				var form = document.createElement("form");
    			    form.setAttribute("method", "post");
    			    form.setAttribute("action", url);
    			    var tokenField = document.createElement("input");
    			    	tokenField.setAttribute("type", "hidden");
    			    	tokenField.setAttribute("name", "token");
    			    	tokenField.setAttribute("value", token);
    			    	form.appendChild(tokenField);
  			    	var button = document.createElement("input");
  			    	  	button.setAttribute('type', "submit");
  			    		form.appendChild(button);
    			    form.submit();
    			}
    		}
    		
    		$('.btn-login').click(function(e){
    			e.preventDefault();
    			loginPost(2);
    		});
    		
    		var getExpiry = function(){
    			var date = new Date();
    			var expiry = new Date(date.getTime() + minutes*60000);
    			return expiry.yyyymmdd();
    		}
    	})
	</script>
</body>
</html>