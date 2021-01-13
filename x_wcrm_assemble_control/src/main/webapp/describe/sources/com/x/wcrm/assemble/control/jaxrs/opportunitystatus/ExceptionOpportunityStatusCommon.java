package com.x.wcrm.assemble.control.jaxrs.opportunitystatus;

import com.x.base.core.project.exception.PromptException;

public class ExceptionOpportunityStatusCommon extends PromptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6420524764278817441L;

	public ExceptionOpportunityStatusCommon(String description) {
		super("商机状态：" + description);
	}
}
