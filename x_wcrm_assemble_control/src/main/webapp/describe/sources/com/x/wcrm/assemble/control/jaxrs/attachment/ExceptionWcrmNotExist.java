package com.x.wcrm.assemble.control.jaxrs.attachment;

import com.x.base.core.project.exception.PromptException;

class ExceptionWcrmNotExist extends PromptException {

	private static final long serialVersionUID = -7038279889683420366L;

	ExceptionWcrmNotExist(String workId) {
		super("CRM work id:{}, not existed.", workId);
	}

}
