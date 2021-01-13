package com.x.wcrm.assemble.control.jaxrs.opportunity;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.organization.OrganizationDefinition;
import com.x.base.core.project.organization.OrganizationDefinition.DistinguishedNameCategory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.service.ContactsService;
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.service.LeadsService;
import com.x.wcrm.assemble.control.service.OperationRecordService;
import com.x.wcrm.assemble.control.service.OpportunityPermissionService;
import com.x.wcrm.assemble.control.service.OpportunityService;

public class BaseAction extends StandardJaxrsAction {
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(BaseAction.class);

	protected static final String OWNER_ROLE = "负责人";
	protected static final String TEAMMEMBERS_ROLE = "团队成员";

	protected static final String OWNER_PERMISSION = "负责人权限";
	protected static final String READWRITE_PERMISSION = "读写";
	protected static final String READONLY_PERMISSION = "只读";

	protected List<String> convertToPerson(Business business, List<String> list) throws Exception {
		List<String> os = new ArrayList<>();
		DistinguishedNameCategory category = OrganizationDefinition.distinguishedNameCategory(list);
		os.addAll(business.organization().person().list(category.getPersonList()));
		os.addAll(business.organization().person().listWithIdentity(category.getIdentityList()));
		os.addAll(business.organization().person().listWithUnitSubDirect(category.getUnitList()));
		os = ListTools.trim(os, true, true);
		return os;
	}

	//	static void initDefaultValue(EffectivePerson effectivePerson, Contacts contacts) {
	//		if (null == contacts.getDecision() || StringUtils.isBlank(contacts.getDecision())) {
	//			/*是否为关键决策人0 不是，1是*/
	//			contacts.setDecision("0");
	//		}
	//		
	//		if (null == contacts.getSex() || StringUtils.isBlank(contacts.getSex())) {
	//			/*默认性别男*/
	//			contacts.setSex("男");
	//		}
	//	}

	LeadsService leadsService = new LeadsService();

	CustomerService customerService = new CustomerService();

	ContactsService contactsService = new ContactsService();

	OpportunityService opportunityService = new OpportunityService();

	OperationRecordService operationRecordService = new OperationRecordService();

	OpportunityPermissionService opportunityPermissionService = new OpportunityPermissionService();
}
