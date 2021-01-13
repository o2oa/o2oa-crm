package com.x.wcrm.assemble.control.jaxrs.customer;

import com.x.base.core.project.exception.PromptException;

class ExceptionCustomerNoPermission extends PromptException {
	private static final long serialVersionUID = -4953155511190071404L;

	ExceptionCustomerNoPermission(String id) {
		super("不是，ID:" + id+"的负责人。或者不是管理员角色。");
	}
}
