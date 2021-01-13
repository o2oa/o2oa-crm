package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/opportunitytype/*", asyncSupported = true)
public class OpportunityTypeJaxrsFilter extends CipherManagerUserJaxrsFilter {

}
