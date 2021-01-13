package com.x.wcrm.assemble.control.jaxrs.leads;

import com.x.base.core.project.exception.PromptException;

class ExceptionLeadsBaseMessage extends PromptException {
	private static final long serialVersionUID = -6862199758614749451L;

	ExceptionLeadsBaseMessage(String message) {
		super(message);
	}
}
