<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("referenceapplication.app.manageExtensions.title") ])

    ui.includeJavascript("referenceapplication", "manageApps.js");

    ui.includeCss("referenceapplication", "manageApps.css");

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
          link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("referenceapplication.app.manageExtensions.title")}"}
    ];
</script>


<table>
    <thead>
    <tr>
        <th>${ ui.message("referenceapplication.app.appExtensionId.label")}</th>
        <th>${ ui.message("referenceapplication.app.status.label")}</th>
        <th>${ ui.message("referenceapplication.app.actions.label")}</th>
    </tr>
    </thead>
    <% extensions.each { extension -> %>
    <tbody>
    <tr>
        <td>${extension.id}</td>
        <td>
            <% if(extension.enabled) { %>
            ${ui.message("referenceapplication.app.status.enabled")}
            <% } else { %>
            ${ui.message("referenceapplication.app.status.disabled")}
            <% } %>
        </td>
          <td>
            <form id="form-${extension.id}" method="POST">
            
                <% if(extension.enabled) { %>
                    <i class="icon-stop stop-action referenceapplication-action"
                       title="${ ui.message("referenceapplication.app.action.disable") }"></i>
                    <input type="hidden" name="id" value="${extension.id}"/>
                    <input type="hidden" name="action" value="disable" />
                <% } else { %>
                    <i class="icon-play play-action referenceapplication-action"
                       title="${ ui.message("referenceapplication.app.action.enable") }"></i>
                    <input type="hidden" name="id" value="${extension.id}"/>
                    <input type="hidden" name="action" value="enable" />
                <% } %>
                
            
            </form>
         
        </td>
    </tr>
    </tbody>
    <% } %>
</table>
