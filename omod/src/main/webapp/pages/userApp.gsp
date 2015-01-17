<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("referenceapplication.app.userApp."+param.action[0]) ])

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
            link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("referenceapplication.app.manageApps.title")}",
            link: "${ui.pageLink("referenceapplication", "manageApps")}"
        },
        { label: "${ ui.message("referenceapplication.app.userApp."+param.action[0])}"}
    ];
</script>

<h2>${ ui.message("referenceapplication.app.userApp."+param.action[0])}</h2>

<form class="simple-form-ui" method="POST" action="userApp.page">
    <input type="hidden" name="action" value="${param.action[0]}" />
    <p>
        <%if(userApp.appId){%>
        <span class="title">
        ${ui.message("referenceapplication.app.appId.label")}:
        </span>&nbsp;${userApp.appId}
        <input type="hidden" name="appId" value="${userApp.appId}" />
        <%} else{%>
        <label for="appId-field">
            <span class="title">
                ${ui.message("referenceapplication.app.appId.label")} (${ ui.message("emr.formValidation.messages.requiredField.label") })
            </span>
        </label>
        <input id="appId-field" type="text" class="required" name="appId" size="80" placeholder="${ ui.message("referenceapplication.app.definition.placeholder") }" />
        <%}%>
    </p>
    <p>
        <label for="json-field">
            <span class="title">
            ${ui.message("referenceapplication.app.definition.label")} (${ ui.message("emr.formValidation.messages.requiredField.label") })
            </span>
        </label>
        <textarea id="json-field" class="required" name="json" rows="15" cols="80">${userApp.json ? userApp.json : ""}</textarea>
    </p>

    <input type="button" class="cancel" value="${ ui.message("general.cancel") }" onclick="javascript:window.location='/${ contextPath }/referenceapplication/manageApps.page'" />
    <input type="submit" class="confirm right" id="save-button" value="${ ui.message("general.save") }"  />
</form>