package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/inputleads/*", asyncSupported = true)
public class LeadsInputJaxrsFilter extends CipherManagerUserJaxrsFilter {
	
}
