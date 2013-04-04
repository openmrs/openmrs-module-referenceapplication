<%
	ui.setPageTitle(ui.message("referenceapplication.home.title"))
	
	def htmlSafeId = { app ->
        "${ app.id.replace(".", "-") }-app"
    }
%>

<div id="home-container">

	<h3>${ui.message("referenceapplication.home.heading")}</h3>
	
    <div id="apps">
        <% apps.each { app -> %>

            <a id="${ htmlSafeId(app) }" href="/${ contextPath }/${ app.url }">
                <% if (app.iconUrl) { %>
                    <i class="${ app.iconUrl }"></i>
                <% } %>
                 ${ ui.message(app.label) }
            </a>

        <% } %>
    </div>

</div>