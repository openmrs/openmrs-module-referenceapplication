<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("referenceapplication.home.title") ])
    ui.includeCss("referenceapplication", "home.css")

    def htmlSafeId = { extension ->
        "${ extension.id.replace(".", "-") }-${ extension.id.replace(".", "-") }-extension"
    }
%>

<div class="row">
    <div class="col-12 col-sm-12 col-md-12 col-lg-12 homeNotification">
        ${ ui.includeFragment("coreapps", "administrativenotification/notifications") }
    </div>
</div>
<div class="row">
    <div class="col-12 col-sm-12 col-md-12 col-lg-12">
     
     <h2>Testing the controller of the jsp</h2>
     <fieldset><legend><openmrs:message code="options.login.legend" /></legend>
     
     fieldset><legend><openmrs:message code="options.login.legend" /></legend>
     
     
     
     
     <form method="post" action="" >

    <label for="male">Male</label>
    <input type="radio" name="gender" id="male" value="male"><br>
    <label for="female">Female</label>
    <input type="radio" name="gender" id="female" value="female"><br>
    <label for="other">Other</label>
    <input type="radio" name="gender" id="other" value="other"><br><br>
    <input type="submit" value="Submit"

    <div class="adminui-section-padded-top">
        <input type="submit" class="confirm right" name="save" id="save-button" value="${ui.message("general.save")}"
               ng-disabled="changePasswordForm.\$invalid" />
        <input type="button" id="cancel-button" class="cancel" value="${ui.message("general.cancel")}"
               onclick="window.location='${ui.pageLink("adminui", "myaccount/myAccount")}'" />
    </div>

</form>
     
     
     
     
     
     
     
     
     
     
     
    </div>
</div>
