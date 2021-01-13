package com.x.wcrm.assemble.control.jaxrs.contacts;

import com.x.base.core.project.exception.PromptException;

class ExceptionContactsBaseMessage extends PromptException {
	private static final long serialVersionUID = -6862199758614749451L;

	ExceptionContactsBaseMessage(String message) {
		super(message);
	}
}
