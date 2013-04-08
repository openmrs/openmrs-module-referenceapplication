<%
	ui.setPageTitle(ui.message("referenceapplication.home.title"))
	
	def htmlSafeId = { extension ->
        "${ extension.appId.replace(".", "-") }-${ extension.id.replace(".", "-") }-extension"
    }
%>

<div id="home-container">

	<h3>${ui.message("referenceapplication.home.heading")}</h3>
	
    <div id="extensions">
    <% extensions.each { ext -> %>
            <a id="${ htmlSafeId(ext) }" href="/${ contextPath }/${ ext.url }">
                ${ ui.message(ext.label) }
            </a>
            <br />
		<% } %>
    </div>

</div>