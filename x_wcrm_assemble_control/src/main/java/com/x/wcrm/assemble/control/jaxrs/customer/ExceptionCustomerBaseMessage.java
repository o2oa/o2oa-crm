package com.x.wcrm.assemble.control.jaxrs.customer;

import com.x.base.core.project.exception.PromptException;

class ExceptionCustomerBaseMessage extends PromptException {
	private static final long serialVersionUID = -6862199758614749451L;

	ExceptionCustomerBaseMessage(String message) {
		super(message);
	}
}
