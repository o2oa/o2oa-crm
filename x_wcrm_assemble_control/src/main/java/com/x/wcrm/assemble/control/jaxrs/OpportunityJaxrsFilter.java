package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/opportunity/*", asyncSupported = true)
public class OpportunityJaxrsFilter extends CipherManagerUserJaxrsFilter {

}
