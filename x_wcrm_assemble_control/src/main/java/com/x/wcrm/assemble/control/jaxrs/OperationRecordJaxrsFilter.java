package com.x.wcrm.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/operationrecord/*", asyncSupported = true)
public class OperationRecordJaxrsFilter extends CipherManagerUserJaxrsFilter {

}
