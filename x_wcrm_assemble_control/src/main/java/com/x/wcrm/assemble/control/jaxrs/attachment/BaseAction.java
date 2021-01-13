package com.x.wcrm.assemble.control.jaxrs.attachment;

import java.util.Date;

import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.wcrm.core.entity.Attachment;

public class BaseAction extends StandardJaxrsAction {
	protected Attachment concreteAttachment(String crm_id) throws Exception {
		Attachment attachment = new Attachment();
		attachment.setWcrm(crm_id);
		attachment.setWorkCreateTime(new Date());
		return attachment;
	}
}
