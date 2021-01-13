package com.x.wcrm.assemble.control.jaxrs.opportunitystatus;

import com.x.base.core.project.exception.PromptException;

public class ExceptionOpportunityTypeIdNull extends PromptException {

	private static final long serialVersionUID = 4366822935668074370L;

	public ExceptionOpportunityTypeIdNull() {
		super("商机组id为空.");
	}
}
