package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/pool/*", asyncSupported = true)
public class PoolJaxrsFilter extends CipherManagerUserJaxrsFilter {

}
