package com.x.wcrm.assemble.control.jaxrs.opportunity;

import com.x.base.core.project.exception.PromptException;

class ExceptionOpportunityBaseMessage extends PromptException {
	private static final long serialVersionUID = -6862199758614749451L;

	ExceptionOpportunityBaseMessage(String message) {
		super(message);
	}
}
