package com.x.wcrm.assemble.control.jaxrs.leads;

import com.x.base.core.project.exception.PromptException;

class ExceptionLeadsNoPermission extends PromptException {
	private static final long serialVersionUID = -4953155511190071404L;

	ExceptionLeadsNoPermission(String id) {
		super("不是，ID:" + id+"的负责人。或者不是管理员角色。");
	}
}
