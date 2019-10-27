<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("referenceapplication.app.manageApps.title") ])

    ui.includeJavascript("referenceapplication", "manageApps.js");

    ui.includeCss("referenceapplication", "manageApps.css");

    ui.includeJavascript("appui", "jquery-3.4.1.min.js")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
          link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("referenceapplication.app.manageApps.title")}"}
    ];
</script>

<% apps.each { app -> %>
    ${ui.includeFragment("referenceapplication", "deleteUserApp", [appId: app.id])}
<% } %>

<h2>${ ui.message("referenceapplication.app.manageApps.heading")}</h2>

<button class="confirm" onclick="location.href='${ ui.pageLink("referenceapplication", "userApp", [action: "add"]) }'">
    ${ ui.message("referenceapplication.app.addAppDefinition") }
</button>
</br></br>

<table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl">
    <thead>
    <tr>
        <th>${ ui.message("referenceapplication.app.appId.label")}</th>
        <th>${ ui.message("referenceapplication.app.status.label")}</th>
        <th>${ ui.message("referenceapplication.app.type.label")}</th>
        <th>${ ui.message("referenceapplication.app.actions.label")}</th>
    </tr>
    </thead>
    <% apps.each { app -> %>
    <tbody>
    <tr>
        <td>${app.id}</td>
        <td>
            <% if(app.enabled) { %>
            ${ui.message("referenceapplication.app.status.enabled")}
            <% } else { %>
            ${ui.message("referenceapplication.app.status.disabled")}
            <% } %>
        </td>
        <td>
            <% if(app.builtIn) { %>
            ${ui.message("referenceapplication.app.type.builtIn")}
            <% } else { %>
            ${ui.message("referenceapplication.app.type.implementationDefined")}
            <% } %>
        </td>
        <td>
            <form id="form-${app.id}" method="POST">
            <% if(!app.cannotBeStopped) { %>
                <% if(app.enabled) { %>
                    <i class="icon-stop stop-action referenceapplication-action"
                       title="${ ui.message("referenceapplication.app.action.disable") }"></i>
                    <input type="hidden" name="id" value="${app.id}"/>
                    <input type="hidden" name="action" value="disable" />
                <% } else { %>
                    <i class="icon-play play-action referenceapplication-action"
                       title="${ ui.message("referenceapplication.app.action.enable") }"></i>
                    <input type="hidden" name="id" value="${app.id}"/>
                    <input type="hidden" name="action" value="enable" />
                <% } %>
                <% if(!app.builtIn) { %>
                    <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
                       onclick="location.href='${ui.pageLink("referenceapplication", "userApp", [appId: app.id, action: "edit"])}';"></i>
                    <i class="icon-remove delete-action" title="${ ui.message("general.delete") }"
                       onclick="showDeleteUserAppDialog('${app.id}')"></i>
                <% } %>
            <% } %>
            </form>
            <% if(app.cannotBeStopped) { %>
                <i class="icon-lock lock-action referenceapplication-action"></i>
            <% } %>
        </td>
    </tr>
    </tbody>
    <% } %>
</table>
