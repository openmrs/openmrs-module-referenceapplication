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
    
     <form method="post" action="" >

    <label for="username">User Name</label>
    <input type="text" name="username" id="username" value=""><br>
    <label for="personName">Person Name</label>
    <input type="text" name="personName" id="personName" value=""><br>
    <label for="oldPassword">Old password</label>
    <input type="password" name="oldPassword" id="oldPassword" value=""><br>
    <label for="newPassword">New password</label>
    <input type="password" name="newPassword" id="newPassword" value=""><br>
    
    <label for="confirmPassword">Confirm Password</label>
    <input type="password" name="confirmPassword" id="confirmPassword" value=""><br>
    <label for="secretQuestionPassword">Secret Question Password</label>
    <input type="password" name="secretQuestionPassword" id="secretQuestionPassword" value=""><br>
    <label for="secretQuestionNew">Secret Question </label>
    <input type="password" name="" id="secretQuestionNew" value=""><br>
    <label for="secretAnswerNew">Secret Answer </label>
    <input type="password" name="" id="secretAnswerNew" value=""><br> 
    <label for="secretAnswerNew">Secret Answer </label>
    <input type="password" name="" id="secretAnswerNew" value=""><br>
    <label for="secretAnswerConfirm">Secret Answer Confirm" </label>
    <input type="password" name="" id="secretAnswerConfirm" value=""><br>




    <input type="submit" value="Submit">
    

</form>
     
     
     
     
     
     
     
     
     
     
     
    </div>
</div>
