package com.x.wcrm.assemble.control.jaxrs.contacts;

import com.x.base.core.project.exception.PromptException;

class ExceptionContactsNoPermission extends PromptException {
	private static final long serialVersionUID = -4953155511190071404L;

	ExceptionContactsNoPermission(String id) {
		super("不是，ID:" + id+"的负责人。或者不是管理员角色。或者联系人关联的客户在公海中。");
	}
}
