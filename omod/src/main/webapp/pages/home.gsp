<%
	ui.setPageTitle(ui.message("referenceapplication.home.title"))
	
	def htmlSafeId = { extension ->
        "${ extension.appId.replace(".", "-") }-${ extension.id.replace(".", "-") }-extension"
    }
%>

<div id="home-container">

	<h3>${ui.message("referenceapplication.home.heading")}</h3>
	
    <div id="extensions">
    <% appExtensionsMap.each { app, extensions -> %>
        <% extensions.each { extension -> %>
            <a id="${ htmlSafeId(extension) }" href="/${ contextPath }/${ extension.url }">
                ${ ui.message(extension.label) }
            </a>
            <br />
		<% } %>
	<% } %>
    </div>

</div>