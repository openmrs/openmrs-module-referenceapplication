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
     
     
     <fieldset><legend><openmrs:message code="options.login.legend" /></legend>
<table>
	<tr>
		<td><openmrs:message code="options.login.username" /></td>
		<td>
			<spring:bind path="opts.username">
				<input type="text" name="${status.expression}" value="${status.value}" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
		</td>
	</tr>
	<spring:nestedPath path="opts.personName">
		<openmrs:portlet url="nameLayout" id="namePortlet" size="full" parameters="layoutMode=edit|layoutShowTable=false|layoutShowExtended=false" />
	</spring:nestedPath>
	<tr><td colspan="2"><br/></td></tr>
	<tr>
		<td><openmrs:message code="options.login.password.old" /></td>
		<td>
			<spring:bind path="opts.oldPassword">
				<input type="password" name="${status.expression}" value="${status.value}${resetPassword}" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><openmrs:message code="options.login.password.new" /></td>
		<td>
			<spring:bind path="opts.newPassword">
				<input type="password" name="${status.expression}"
			value="${status.value}" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
			<%-- Don't print empty brackets --%>
			<c:if test="${passwordHint != ''}">
				(${passwordHint})
			</c:if>
		</td>
	</tr>
	<tr>
		<td><openmrs:message code="options.login.password.confirm" /></td>
		<td>
			<spring:bind path="opts.confirmPassword">
				<input type="password" name="${status.expression}"
			value="${status.value}" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
			<openmrs:message code="User.confirm.description" />
		</td>
	</tr>
	<tr><td colspan="2"><br/></td></tr>
	<tr><td colspan="2"><openmrs:message code="options.login.secretQuestion.about" /></td></tr>
	<tr>
		<td><openmrs:message code="options.login.password.old" /></td>
		<td>
			<spring:bind path="opts.secretQuestionPassword">
				<input type="password" name="${status.expression}" value="${status.value}" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><openmrs:message code="options.login.secretQuestionNew" /></td>
		<td>
			<spring:bind path="opts.secretQuestionNew">
				<input type="text" name="${status.expression}"
					value="${status.value}" size="35"/>
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><openmrs:message code="options.login.secretAnswerNew" /></td>
		<td>
			<spring:bind path="opts.secretAnswerNew">
				<input type="password" name="${status.expression}"
					value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><openmrs:message code="options.login.secretAnswerConfirm" /></td>
		<td>
			<spring:bind path="opts.secretAnswerConfirm">
				<input type="password" name="${status.expression}"
					value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}">
					<span class="error">${status.errorMessage}</span>
				</c:if>
			</spring:bind>
		</td>
	</tr>
	</table>
        
    </div>
</div>
