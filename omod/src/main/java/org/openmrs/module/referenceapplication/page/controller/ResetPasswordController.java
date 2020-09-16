package org.openmrs.module.referenceapplication.page.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.OptionsForm;
import org.openmrs.web.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ResetPasswordController extends SimpleFormController{
	


	
	
	
	
	
	/**
	 * Called prior to form display. Allows for data to be put in the request to be used in the view
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	
	
	 protected Map<String,Object>  referenceData(HttpServletRequest request)  throws  Exception{
		 
		 HttpSession httpSession=request.getSession();
		 Map<String,Object> map=new HashMap<String,Object>();
		 
		 if(Context.isAuthenticated()) {
			 
			 Object resetPasswordAttribute=httpSession.getAttribute("resetPassword");
			 if(resetPasswordAttribute == null) {
				 resetPasswordAttribute= "";
			 }
			 else {
			   httpSession.removeAttribute("resetPassword");
			 
			 }
			 
			 map.put("resetPassword",resetPasswordAttribute);
				
			//generate the password hint depending on the security GP settings
			 
			 List <String>hints=new ArrayList<String>(5);
			 int minChar=1;
			 AdministrationService as = Context.getAdministrationService();
		
			 MessageSourceService mss=Context.getMessageSourceService();
			 
			 try {
				 String minCharStr = as.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH);
					if (StringUtils.isNotBlank(minCharStr)) {
						minChar = Integer.valueOf(minCharStr);
					}
					if (minChar < 1) {
						minChar = 1;
					}
			 }
			 catch(NumberFormatException e) {
				 
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
	 * This is called prior to displaying a form for the first time. It tells Spring the
	 * form/command object to load into the request
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		
		OptionsForm opts=new OptionsForm();
		if(Context.isAuthenticated()) {
			User user=Context.getAuthenticatedUser();	
			opts.setUsername(user.getUsername());
			PersonName personName;
			if(user.getPersonName() !=null) {
			  personName=PersonName.newInstance(user.getPersonName());
			  personName.setPersonNameId(null);
			}
			else {
			  personName=new PersonName();
			 }
			
			opts.setPersonName(personName);
		}
		
		return opts;	
	}
	/**
	 * Utility method that check if a security property with boolean values is enabled and adds hint
	 * message for it if it is not blank
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
 