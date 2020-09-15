package org.openmrs.module.referenceapplication.page.controller;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
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
	


	public ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse  response,Object object,BindException errors)  throws Exception {
		
		
		HttpSession httpSession=request.getSession();
		
		String view=getFormView();
		
		if(!errors.hasErrors()) {
			
			User loginUser=Context.getAuthenticatedUser();
			UserService us=Context.getUserService();
			User user=null;
			
			try {
				
				Context.addProxyPrivilege(PrivilegeConstants.GET_USERS);
				user=us.getUser(loginUser.getId());
			 }
			finally {
				Context.removeProxyPrivilege(PrivilegeConstants.GET_USERS);
			}
			
	  OptionsForm opts=(OptionsForm)object;
	  
	  Map<String ,String> properties=user.getUserProperties();
	  
	  
	  properties.put(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION,opts.getDefaultLocation());
	  
	  
	  Locale locale=WebUtil.normalizeLocale(opts.getDefaultLocale());
	  if(locale !=null) {
			properties.put(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE, locale.toString());
			
	  }
	  
	  properties.put(OpenmrsConstants.USER_PROPERTY_PROFICIENT_LOCALES,
			    WebUtil.sanitizeLocales(opts.getProficientLocales()));
	  properties.put(OpenmrsConstants.USER_PROPERTY_SHOW_RETIRED, opts.getShowRetiredMessage().toString());
	  properties.put(OpenmrsConstants.USER_PROPERTY_SHOW_VERBOSE, opts.getVerbose().toString());
	  
	  properties.put(OpenmrsConstants.USER_PROPERTY_NOTIFICATION, opts.getNotification() == null ? "" : opts
		        .getNotification().toString());
		properties.put(OpenmrsConstants.USER_PROPERTY_NOTIFICATION_ADDRESS, opts.getNotificationAddress() == null ? ""
		        : opts.getNotificationAddress().toString());
		
			
			
			
			
		}
		view = getSuccessView();
		
		
		return new ModelAndView(new RedirectView(view));

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
	
	
}
 