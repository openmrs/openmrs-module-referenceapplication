<%
	ui.setPageTitle(ui.message("referenceapplication.login.title"))
%>

${ ui.includeFragment("referenceapplication", "infoAndErrorMessages") }

<script type="text/javascript">
	\$(document).ready(function(){
		\$('#username').focus();
	});
</script>

<header>
    <div class="logo">
        <a href="${ui.pageLink("referenceapplication", "home")}">
            <img src="${ui.resourceLink("referenceapplication", "images/openMrsLogo.png")}"/>
        </a>
    </div>
</header>

<div id="body-wrapper">
    <div id="content">
        <form id="login-form" method="post" autocomplete="off">
            <fieldset>

                <legend>
                    ${ ui.message("referenceapplication.login.loginHeading") }
                </legend>
				
                <p class="left">
                    <label for="username">
                        ${ ui.message("referenceapplication.login.username") }:
                    </label>
                    <input id="username" type="text" name="username" placeholder="${ ui.message("referenceapplication.login.username.placeholder") }"/>
                </p>

                <p class="left">
                    <label for="password">
                        ${ ui.message("referenceapplication.login.password") }:
                    </label>
                    <input id="password" type="password" name="password" placeholder="${ ui.message("referenceapplication.login.password.placeholder") }"/>
                </p>

                <p>
                    <input id="login-button" class="confirm" type="submit" value="${ ui.message("referenceapplication.login.button") }"/>
                </p>

            </fieldset>
    
    		<input type="hidden" name="redirectUrl" value="${redirectUrl}" />

        </form>

    </div>
</div>