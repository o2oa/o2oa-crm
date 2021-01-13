package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/contactsopportunity/*", asyncSupported = true)
public class ContactsOpportunityJaxrsFilter extends CipherManagerUserJaxrsFilter {

}
