<%
	ui.setPageTitle(ui.message("referenceapplication.home.title"))
	
	def htmlSafeId = { extension ->
        "${ extension.id.replace(".", "-") }-${ extension.id.replace(".", "-") }-extension"
    }
%>

<div id="home-container">

	<h3>${ui.message("referenceapplication.home.heading")}</h3>

    <% if (authenticatedUser) { %>
        <h4>
            ${ ui.message("referenceapplication.home.currentUser", ui.format(authenticatedUser)) }
            <% if (authenticatedUser) { %>
                <a href="logout">${ ui.message("referenceapplication.home.logOut") }</a>
            <% } %>
        </h4>
    <% } else { %>
        <h4>
            <a href="login.htm">${ ui.message("referenceapplication.home.logIn") }</a>
        </h4>
    <% } %>

    <div id="extensions">
    <% extensions.each { ext -> %>
            <a id="${ htmlSafeId(ext) }" href="/${ contextPath }/${ ext.url }">
                <% if (ext.icon) { %>
                    <i class="${ ext.icon }"></i>
                <% } %>
                ${ ui.message(ext.label) }
            </a>
            <br />
		<% } %>
    </div>

</div>