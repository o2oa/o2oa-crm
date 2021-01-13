package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/contacts/*", asyncSupported = true)
public class ContactsJaxrsFilter extends CipherManagerUserJaxrsFilter {

}
