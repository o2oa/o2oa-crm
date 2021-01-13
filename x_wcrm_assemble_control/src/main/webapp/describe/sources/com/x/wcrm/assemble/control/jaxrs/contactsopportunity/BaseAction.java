package com.x.wcrm.assemble.control.jaxrs.contactsopportunity;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.organization.OrganizationDefinition;
import com.x.base.core.project.organization.OrganizationDefinition.DistinguishedNameCategory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.service.ContactsService;
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.service.LeadsService;
import com.x.wcrm.assemble.control.service.OperationRecordService;
import com.x.wcrm.assemble.control.service.OpportunityService;

public class BaseAction extends StandardJaxrsAction {

	protected List<String> convertToPerson(Business business, List<String> list) throws Exception {
		List<String> os = new ArrayList<>();
		DistinguishedNameCategory category = OrganizationDefinition.distinguishedNameCategory(list);
		os.addAll(business.organization().person().list(category.getPersonList()));
		os.addAll(business.organization().person().listWithIdentity(category.getIdentityList()));
		os.addAll(business.organization().person().listWithUnitSubDirect(category.getUnitList()));
		os = ListTools.trim(os, true, true);
		return os;
	}

	
	LeadsService leadsService = new LeadsService();

	CustomerService customerService = new CustomerService();
	
	ContactsService contactsService = new ContactsService();
	
	OpportunityService opportunityService = new OpportunityService();
	
	OperationRecordService operationRecordService = new OperationRecordService();
}
