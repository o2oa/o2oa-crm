package com.x.wcrm.assemble.control.jaxrs;

import java.util.Set;

import javax.ws.rs.ApplicationPath;

import com.x.base.core.project.jaxrs.AbstractActionApplication;
import com.x.wcrm.assemble.control.jaxrs.attachment.AttachmentAction;
import com.x.wcrm.assemble.control.jaxrs.common.CommonAction;
import com.x.wcrm.assemble.control.jaxrs.contacts.ContactsAction;
import com.x.wcrm.assemble.control.jaxrs.contactsopportunity.ContactsOpportunityAction;
import com.x.wcrm.assemble.control.jaxrs.customer.CustomerAction;
import com.x.wcrm.assemble.control.jaxrs.customer.pool.PoolAction;
import com.x.wcrm.assemble.control.jaxrs.inputleads.InputLeadsAction;
import com.x.wcrm.assemble.control.jaxrs.leads.LeadsAction;
import com.x.wcrm.assemble.control.jaxrs.operationrecord.OperationRecordAction;
import com.x.wcrm.assemble.control.jaxrs.opportunity.OpportunityAction;
import com.x.wcrm.assemble.control.jaxrs.opportunitystatus.OpportunityStatusAction;
import com.x.wcrm.assemble.control.jaxrs.opportunitytype.OpportunityTypeAction;
import com.x.wcrm.assemble.control.jaxrs.record.RecordAction;
import com.x.wcrm.assemble.control.jaxrs.statistic.StatisticAction;

@ApplicationPath("jaxrs")
public class ActionApplication extends AbstractActionApplication {

	public Set<Class<?>> getClasses() {
		classes.add(CommonAction.class);
		classes.add(LeadsAction.class); // 线索
		classes.add(InputLeadsAction.class); // 线索导入
		classes.add(CustomerAction.class);// 客户
		classes.add(PoolAction.class);// 客户公海
		classes.add(ContactsAction.class);// 联系人
		classes.add(ContactsOpportunityAction.class);// 联系人，商机关系表
		classes.add(OpportunityAction.class);// 商机
		classes.add(OpportunityStatusAction.class);// 商机状态
		classes.add(OpportunityTypeAction.class);// 商机状态组
		classes.add(AttachmentAction.class);// 附件
		classes.add(RecordAction.class);// 跟进记录
		classes.add(OperationRecordAction.class);// 跟进记录

		classes.add(StatisticAction.class);// 统计

		return this.classes;
	}

}