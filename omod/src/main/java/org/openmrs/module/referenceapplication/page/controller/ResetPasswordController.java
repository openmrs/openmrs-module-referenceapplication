package org.openmrs.module.referenceapplication.page.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PasswordException;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.OptionsForm;
import org.openmrs.web.WebConstants;
import org.openmrs.web.user.UserProperties;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ResetPasswordController extends SimpleFormController {

	/** Logger for this class and subclasses */

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */

	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
			Object object, BindException errors) throws Exception {

		OptionsForm opts = (OptionsForm) object;

		if (!"".equals(opts.getOldPassword())) {
			if ("".equals(opts.getNewPassword())) {

				errors.rejectValue("newPassword", "error.password.weak");
			} else if (!opts.getNewPassword().equals(opts.getConfirmPassword())) {
				errors.rejectValue("newPassword", "error.password.match");
				errors.rejectValue("confirmPassword", "error.password.match");
			}

		}

		if ("".equals(opts.getSecretQuestionPassword()) && opts.getSecretAnswerNew().isEmpty()

				&& !opts.getSecretQuestionNew().equals(opts.getSecretQuestionCopy())) {
			errors.rejectValue("secretQuestionPassword", "error.password.incorrect");

		}

		if (!"".equals(opts.getSecretQuestionPassword())) {

			if (!opts.getSecretAnswerConfirm().equals(opts.getSecretAnswerNew())) {
				errors.rejectValue("secretAnswerNew", "error.options.secretAnswer.match");
				errors.rejectValue("secretAnswerConfirm", "error.options.secretAnswer.match");

			}

			if (opts.getSecretAnswerNew().isEmpty()) {
				errors.rejectValue("secretAnswerNew", "error.options.secretAnswer.empty");

			}
			if (opts.getSecretQuestionNew().isEmpty()) {
				errors.rejectValue("secretQuestionNew", "error.options.secretQuestion.empty");

			}

		}
		return super.processFormSubmission(request, response, object, errors);
	}

	/**
	 * The onSubmit function receives the form/command object that was modified by
	 * the input form and saves it to the db
	 * 
	 */

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
			BindException errors) throws Exception {

		HttpSession httpSession = request.getSession();

		String view = getFormView();

		if (!errors.hasErrors()) {
			User loginUser = Context.getAuthenticatedUser();
			UserService userService = Context.getUserService();
			User user = null;

			try {
				Context.addProxyPrivilege(PrivilegeConstants.GET_USERS);

				user = userService.getUser(loginUser.getUserId());

			} finally {

				Context.removeProxyPrivilege(PrivilegeConstants.GET_USERS);
			}
			OptionsForm opts = (OptionsForm) obj;

			if (!"".equals(opts.getOldPassword())) {
				try {
					String password = opts.getNewPassword();
                    // checking the password strength
					if (password.length() > 0) {
						try {
							OpenmrsUtil.validatePassword(user.getUsername(), password,
									String.valueOf(user.getUserId()));

						} catch (PasswordException e) {
							errors.reject(e.getMessage());

						}
						if (password.equals(opts.getOldPassword()) && !errors.hasErrors()) {
							errors.reject("error.password.different");
						}
						if (!password.equals(opts.getConfirmPassword())) {
							errors.reject("error.password.match");
						}
					}
					if (!errors.hasErrors()) {
                        userService.changePassword(opts.getOldPassword(), password);
						if (opts.getSecretQuestionPassword().equals(opts.getOldPassword())) {
							opts.setSecretQuestionPassword(password);
						}
						new UserProperties(user.getUserProperties()).setSupposedToChangePassword(false);
					}

				} catch (APIException e) {
					errors.rejectValue("oldPassword", "error.password.match");
				}

			} else {
              	// if they left the old password blank but filled in new
				// password
				if (!"".equals(opts.getNewPassword())) {
					errors.rejectValue("oldPassword", "error.password.incorrect");
				}

			}

			if (!"".equals(opts.getSecretQuestionPassword())) {
				if (!errors.hasErrors()) {
					try {
						userService.changeQuestionAnswer(opts.getSecretQuestionPassword(), opts.getSecretQuestionNew(),
								opts.getSecretAnswerNew());
					} catch (APIException e) {
						errors.rejectValue("secretQuestionPassword", "error.password.match");
					}
				}
			}
			else if (!"".equals(opts.getSecretAnswerNew())) {
				errors.rejectValue("secretQuestionPassword", "error.password.incorrect");
			}
			if (opts.getUsername().length() > 0 && !errors.hasErrors()) {

				try {
					Context.addProxyPrivilege(PrivilegeConstants.GET_USERS);

					if (userService.hasDuplicateUsername(user)) {

						errors.rejectValue("username", "error.username.taken");
					}

				} finally {
					Context.removeProxyPrivilege(PrivilegeConstants.GET_USERS);
				}
			}

			if (!errors.hasErrors()) {
				user.setUsername(opts.getUsername());

				// new name
				PersonName newPersonName = opts.getPersonName();
				// existing name
				PersonName existingPersonName = user.getPersonName();

				// if two are not equal then make the new one the preferred,
				// make the old one voided

				if (!existingPersonName.equalsContent(newPersonName)) {
					existingPersonName.setPreferred(false);
					existingPersonName.setVoided(true);
					existingPersonName.setVoidedBy(user);
					existingPersonName.setDateVoided(new Date());
					existingPersonName.setVoidReason("Changed name on own options form");
					newPersonName.setPreferred(true);
					user.addName(newPersonName);
				}

				Errors userErrors = new BindException(user, "user");
				if (userErrors.hasErrors()) {
					for (ObjectError error : userErrors.getAllErrors()) {
						errors.reject(error.getCode(), error.getArguments(), "");
					}
				}

				if (errors.hasErrors()) {
					return super.processFormSubmission(request, response, opts, errors);

				}

				try {
					Context.addProxyPrivilege(PrivilegeConstants.EDIT_USERS);
					Context.addProxyPrivilege(PrivilegeConstants.GET_USERS);

					userService.saveUser(user);
					// update login user object so that the new name is visible
					// in the webapp
					Context.refreshAuthenticatedUser();

				} finally {

					Context.removeProxyPrivilege(PrivilegeConstants.EDIT_USERS);
					Context.removeProxyPrivilege(PrivilegeConstants.GET_USERS);

				}

				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "options.saved");

			} else {
				return super.processFormSubmission(request, response, opts, errors);
			}
			view = getSuccessView();
		}

		return new ModelAndView(new RedirectView(view));
	}

	/**
	 * Called prior to form display. Allows for data to be put in the request to be
	 * used in the view
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */

	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {

		HttpSession httpSession = request.getSession();
		Map<String, Object> map = new HashMap<String, Object>();

		if (Context.isAuthenticated()) {

			Object resetPasswordAttribute = httpSession.getAttribute("resetPassword");
			if (resetPasswordAttribute == null) {
				resetPasswordAttribute = "";
			} else {
				httpSession.removeAttribute("resetPassword");

			}

			map.put("resetPassword", resetPasswordAttribute);

			// generate the password hint depending on the security GP settings

			List<String> hints = new ArrayList<String>(5);
			int minChar = 1;
			AdministrationService as = Context.getAdministrationService();

			MessageSourceService mss = Context.getMessageSourceService();

			try {
				String minCharStr = as.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH);
				if (StringUtils.isNotBlank(minCharStr)) {
					minChar = Integer.valueOf(minCharStr);
				}
				if (minChar < 1) {
					minChar = 1;
				}
			} catch (NumberFormatException e) {

			}

			hints.add(mss.getMessage("options.login.password.minCharacterCount", new Object[] { minChar }, null));
			addHint(hints, as.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_CANNOT_MATCH_USERNAME_OR_SYSTEMID),
					mss.getMessage("options.login.password.cannotMatchUsername"));
			addHint(hints, as.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_UPPER_AND_LOWER_CASE),
					mss.getMessage("options.login.password.containUpperCase"));
			addHint(hints, as.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_DIGIT),
					mss.getMessage("options.login.password.containNumber"));
			addHint(hints, as.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_NON_DIGIT),
					mss.getMessage("options.login.password.containNonNumber"));

			StringBuilder passwordHint = new StringBuilder("");
			for (int i = 0; i < hints.size(); i++) {
				if (i == 0) {
					passwordHint.append(hints.get(i));
				} else if (i < (hints.size() - 1)) {
					passwordHint.append(", ").append(hints.get(i));
				} else {
					passwordHint.append(" and ").append(hints.get(i));
				}
			}

			map.put("passwordHint", passwordHint.toString());

		}
		return map;

	}

	/**
	 * This is called prior to displaying a form for the first time. It tells Spring
	 * the form/command object to load into the request
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		OptionsForm opts = new OptionsForm();
		if (Context.isAuthenticated()) {
			User user = Context.getAuthenticatedUser();
			opts.setUsername(user.getUsername());
			PersonName personName;
			if (user.getPersonName() != null) {
				personName = PersonName.newInstance(user.getPersonName());
				personName.setPersonNameId(null);
			} else {
				personName = new PersonName();
			}
			opts.setPersonName(personName);
		}
		return opts;
	}

	/**
	 * Utility method that check if a security property with boolean values is
	 * enabled and adds hint message for it if it is not blank
	 * 
	 * @param hints
	 * @param gpValue the value of the global property
	 * @param message the localized message to add
	 */
	private void addHint(List<String> hints, String gpValue, String message) {
		if (Boolean.valueOf(gpValue) && !StringUtils.isBlank(message)) {
			hints.add(message);
		}
	}

}
