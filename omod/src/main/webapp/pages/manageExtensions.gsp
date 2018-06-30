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

<% extensions.each { extension -> %>
    ${ui.includeFragment("referenceapplication", "deleteUserApp", [appId: extension.id])}
<% } %>

<h2>${ ui.message("referenceapplication.app.manageExtensions.heading")}</h2>

<button class="confirm" onclick="location.href='${ui.pageLink("referenceapplication", "userApp", [action: "add"]) }'">
   
    ${ ui.message("referenceapplication.extension.addExtensionDefinition") }
</button>
</br></br>


<table>


<thead>


<tr>


 <th>${ ui.message("referenceapplication.app.extensionId.label")}</th>
        <th>${ ui.message("referenceapplication.extension.status.label")}</th>
        <th>${ ui.message("referenceapplication.extension.type.label")}</th>
        <th>${ ui.message("referenceapplication.extensions.actions.label")}</th>
  
  
</tr>
</thead>
<% extensions.each { extension->%>



<tbody>
<tr>
<td> ${extension.id}</td>

</tr>

</tbody>
<%}%>
</table>
