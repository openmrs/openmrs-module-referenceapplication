<%
	def htmlSafeId = { app ->
        "${ app.id.replace(".", "-") }-app"
    }
%>

<div id="home-container">

    <div id="apps">
        <% apps.each { app -> %>

            <a id="${ htmlSafeId(app) }" href="/${ contextPath }/${ app.url }">
                <% if (app.iconUrl) { %>
                    <i class="${ app.iconUrl }"></i>
                <% } %>
                ${ app.label }
            </a>

        <% } %>
    </div>

</div>