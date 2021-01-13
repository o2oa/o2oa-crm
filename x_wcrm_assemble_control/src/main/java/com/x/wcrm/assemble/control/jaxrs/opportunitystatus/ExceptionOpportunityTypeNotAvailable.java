package com.x.wcrm.assemble.control.jaxrs.opportunitystatus;

import com.x.base.core.project.exception.PromptException;

public class ExceptionOpportunityTypeNotAvailable extends PromptException {

	private static final long serialVersionUID = 4366822935668074370L;

	public ExceptionOpportunityTypeNotAvailable() {
		super("商机组id不可以用（系统中没有此id的商机组）.");
	}
}
