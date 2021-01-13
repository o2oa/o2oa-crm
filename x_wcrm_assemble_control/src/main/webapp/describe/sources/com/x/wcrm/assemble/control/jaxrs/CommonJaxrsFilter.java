package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.AnonymousCipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/common/*", asyncSupported = true)
//public class CommonJaxrsFilter extends CipherManagerUserJaxrsFilter {
public class CommonJaxrsFilter extends AnonymousCipherManagerUserJaxrsFilter{

}
