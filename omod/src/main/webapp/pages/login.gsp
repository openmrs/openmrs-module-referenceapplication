<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeJavascript("appui", "jquery.min.js")
    ui.includeJavascript("appui", "popper.min.js")
    ui.includeJavascript("appui", "bootstrap.min.js")
%>

<!DOCTYPE html>
<html>
<head>
    <title>${ ui.message("referenceapplication.login.title") }</title>
    <link rel="shortcut icon" type="image/ico" href="/${ ui.contextPath() }/images/openmrs-favicon.ico"/>
    <link rel="icon" type="image/png\" href="/${ ui.contextPath() }/images/openmrs-favicon.png"/>
    <!-- Latest compiled and minified CSS -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <% ui.includeCss("appui", "bootstrap.min.css") %>
    ${ ui.resourceLinks() }
</head>
<body>
<script type="text/javascript">
    var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
</script>


<% if(showSessionLocations) { %>
<script type="text/javascript">
    jQuery(function() {
    	updateSelectedOption = function() {
	        jQuery('#sessionLocation li').removeClass('selected');
	        
			var sessionLocationVal = jQuery('#sessionLocationInput').val();
	        if(sessionLocationVal != null && sessionLocationVal != "" && sessionLocationVal != 0){
	            jQuery('#sessionLocation li[value|=' + sessionLocationVal + ']').addClass('selected');
	        }
    	};
    
        updateSelectedOption();

        jQuery('#sessionLocation li').click( function() {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });
		jQuery('#sessionLocation li').focus( function() {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });
		
		// If <Enter> Key is pressed, submit the form
		jQuery('#sessionLocation').keyup(function (e) {
    		var key = e.which || e.keyCode;
    		if (key === 13) {
      			jQuery('#login-form').submit();
    		}
		});
		var  listItem = Array.from(jQuery('#sessionLocation li'));
		for (var i in  listItem){
			 listItem[i].setAttribute('data-key', i);
			 listItem[i].addEventListener('keyup', function (event){
				var keyCode = event.which || event.keyCode;
				switch (keyCode) {
					case 37: // move left
						jQuery(this).prev('#sessionLocation li').focus();
						break;
					case 39: // move right
						jQuery(this).next('#sessionLocation li').focus();
						break;
					case 38: // move up
						jQuery('#sessionLocation li[data-key=' +(Number(jQuery(document.activeElement).attr('data-key')) - 3) + ']').focus(); 
						break;
					case 40: //	move down
						jQuery('#sessionLocation li[data-key=' +(Number(jQuery(document.activeElement).attr('data-key')) + 3) + ']').focus(); 
						break;
					default: break;
				}
			});
		}
		
        jQuery('#loginButton').click(function(e) {
        	var sessionLocationVal = jQuery('#sessionLocationInput').val();
        	
        	if (!sessionLocationVal) {
       			jQuery('#sessionLocationError').show(); 		
        		e.preventDefault();
        	}
        });
    });
</script>
<% } %>

<script type="text/javascript">
    jQuery(function() {
        var cannotLoginController = emr.setupConfirmationDialog({
            selector: '#cannotLoginPopup',
            actions: {
                confirm: function() {
                    cannotLoginController.close();
                }
            }
        });
        
		jQuery('#username').focus();
        jQuery('a#cantLogin').click(function() {
            cannotLoginController.show();
        });
        
        pageReady = true;
    });
</script>

<script type="text/javascript">
    jq(document).ready(function () {
        if(jq("#clientTimezone").length){
            jq("#clientTimezone").val(Intl.DateTimeFormat().resolvedOptions().timeZone)
        }
    });
</script>

<div id="content" class="container-fluid">
    <div class= "row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            ${ ui.includeFragment("referenceapplication", "infoAndErrorMessages") }
        </div>
    </div>
    <div class= "row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <header>
                <div class="logo">
                    <a href="${ui.pageLink("referenceapplication", "home")}">
                        <img src="${ui.resourceLink("referenceapplication", "images/openMrsLogo.png")}"/>
                    </a>
                </div>
            </header>
        </div>
    </div>
    <div class= "row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <div id="body-wrapper">
                <div id="content">
                    <form id="login-form" method="post" autocomplete="off">
                        <fieldset class="border p-2">

                            <legend class="w-auto">
                                <i class="icon-lock small"></i>
                                ${ ui.message(selectLocation ? "referenceapplication.login.sessionLocation" : "referenceapplication.login.loginHeading") }
                            </legend>

                            <% if(!selectLocation) { %>
                            <p class="left">
                                <label for="username">
                                    ${ ui.message("referenceapplication.login.username") }:
                                </label>
                                <input class="form-control form-control-sm form-control-lg form-control-md" id="username" type="text" name="username" placeholder="${ ui.message("referenceapplication.login.username.placeholder") }"/>
                            </p>

                            <p class="left">
                                <label for="password">
                                    ${ ui.message("referenceapplication.login.password") }:
                                </label>
                                <input class="form-control form-control-sm form-control-lg form-control-md" id="password" type="password" name="password" placeholder="${ ui.message("referenceapplication.login.password.placeholder") }"/>
                            </p>
                            <% } %>

                            <% if(showSessionLocations) { %>
                            <p class="clear">
                                <label for="sessionLocation">
                                    <% if(!selectLocation) { %>${ ui.message("referenceapplication.login.sessionLocation") }: <% } %><span class="location-error" id="sessionLocationError" style="display: none">${ui.message("referenceapplication.login.error.locationRequired")}</span>
                                </label>
                                <ul id="sessionLocation" class="select">
                                    <% locations.sort { ui.format(it) }.each { %>
                                    <li id="${ui.encodeHtml(it.name)}" tabindex="0"  value="${it.id}">${ui.encodeHtmlContent(ui.format(it))}</li>
                                    <% } %>
                                </ul>
                            <% if (ui.convertTimezones()) { %>
                                <input type="hidden" id="clientTimezone" name="clientTimezone">
                            <%} %>
                            </p>

                            <input type="hidden" id="sessionLocationInput" name="sessionLocation"
                                <% if (lastSessionLocation != null) { %> value="${lastSessionLocation.id}" <% } %> />

                            <p></p>
                            <% } %>
                            <p>
                            <% if(selectLocation) {%>
                                <input id="cancelButton" class="btn cancel" type="button"
                                    onclick="javascript:window.location = '/${ contextPath }/logout'"
                                    value="${ ui.message("general.cancel") }" />&nbsp;&nbsp;
                            <% } %>
                                <input id="loginButton" class="btn ${ ui.message(selectLocation ? "btn-success" : "confirm") }" type="submit"
                                    value="${ ui.message(selectLocation ? "general.done" : "referenceapplication.login.button") }"/>
                            </p>
                            <% if(!selectLocation) {%>
                            <p>
                                <a id="cantLogin" href="javascript:void(0)">
                                    <i class="icon-question-sign small"></i>
                                    ${ ui.message("referenceapplication.login.cannotLogin") }
                                </a>
                            </p>
                            <% } %>
                        </fieldset>
                        <input type="hidden" name="redirectUrl" value="${ui.encodeHtmlAttribute(redirectUrl)}" />

                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class= "row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <div id="cannotLoginPopup" class="dialog" style="display: none">
                <div class="dialog-header">
                    <i class="icon-info-sign"></i>
                    <h3>${ ui.message("referenceapplication.login.cannotLogin") }</h3>
                </div>
                <div class="dialog-content">
                    <p class="dialog-instructions">${ ui.message("referenceapplication.login.cannotLoginInstructions") }</p>

                    <button class="confirm">${ ui.message("referenceapplication.okay") }</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
