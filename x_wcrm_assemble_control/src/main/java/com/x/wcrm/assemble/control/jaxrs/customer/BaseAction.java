package com.x.wcrm.assemble.control.jaxrs.customer;

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
import com.x.wcrm.assemble.control.service.CustomerPermissionService;
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.service.LeadsService;
import com.x.wcrm.assemble.control.service.OperationRecordService;
import com.x.wcrm.assemble.control.service.OpportunityService;

public class BaseAction extends StandardJaxrsAction {
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
		//os = ListTools.trim(os, true, true);
		os = ListTools.trim(os, false, false);
		return os;
	}

	public void orgTest(Business business, String _distinguishName) throws Exception {

		logger.info("person:" + business.organization().person().getObject(_distinguishName).toString());
		logger.info("unit:" + business.organization().unit().listWithPerson(_distinguishName));
		List<String> _list = new ArrayList<>();
		_list.add(_distinguishName);
		logger.info("identity().listObject(_list)" + business.organization().identity().listObject(_list));
		logger.info("identity().listWithPerson" + business.organization().identity().listWithPerson(_list));
		logger.info("identity().listObject2222" + business.organization().identity().listObject(business.organization().identity().listWithPerson(_list)));
		logger.info("getWithIdentity" + business.organization().unit().listWithIdentity(business.organization().identity().list(_distinguishName)));

	}

	//	static void initDefaultValue(EffectivePerson effectivePerson, Customer customer) {
	//		if (null == customer.getIslock() || StringUtils.isBlank(customer.getIslock())) {
	//			// 0未锁定，1锁定
	//			customer.setIslock("0");
	//		}
	//		if (null == customer.getDealstatus() || StringUtils.isBlank(customer.getDealstatus())) {
	//			// 成交，未成交
	//			customer.setDealstatus("未成交");
	//		}
	//	}

	protected LeadsService leadsService = new LeadsService();

	protected CustomerService customerService = new CustomerService();

	protected ContactsService contactsService = new ContactsService();

	protected OpportunityService opportunityService = new OpportunityService();

	protected OperationRecordService operationRecordService = new OperationRecordService();

	protected CustomerPermissionService customerPermissionService = new CustomerPermissionService();

}
