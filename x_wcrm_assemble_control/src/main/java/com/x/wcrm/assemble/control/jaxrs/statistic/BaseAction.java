package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.organization.OrganizationDefinition;
import com.x.base.core.project.organization.OrganizationDefinition.DistinguishedNameCategory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.service.ContactsPermissionService;
import com.x.wcrm.assemble.control.service.ContactsService;
import com.x.wcrm.assemble.control.service.CustomerPermissionService;
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.service.CustomerStatisticPermissionService;
import com.x.wcrm.assemble.control.service.LeadsPermissionService;
import com.x.wcrm.assemble.control.service.LeadsService;
import com.x.wcrm.assemble.control.service.OperationRecordService;
import com.x.wcrm.assemble.control.service.OpportunityPermissionService;
import com.x.wcrm.assemble.control.service.OpportunityService;
import com.x.wcrm.assemble.control.service.PermissionServiceBase;
import com.x.wcrm.assemble.control.service.RecordPermissonService;
import com.x.wcrm.assemble.control.service.RecordService;

/**
 * 统计模块BaseAction
 */
public class BaseAction extends StandardJaxrsAction {
	private final Logger logger = LoggerFactory.getLogger(BaseAction.class);

	protected List<String> convertToPerson(Business business, List<String> list) throws Exception {
		List<String> os = new ArrayList<>();
		DistinguishedNameCategory category = OrganizationDefinition.distinguishedNameCategory(list);

		os.addAll(business.organization().person().list(category.getPersonList()));
		os.addAll(business.organization().person().listWithIdentity(category.getIdentityList()));
		//os.addAll(business.organization().person().listWithUnitSubDirect(category.getUnitList()));
		os.addAll(business.organization().person().listWithUnitSubNested(category.getUnitList()));
		// os = ListTools.trim(os, true, true);
		os = ListTools.trim(os, false, false);
		return os;
	}

	protected LeadsService leadsService = new LeadsService();

	protected LeadsPermissionService leadsPermissionService = new LeadsPermissionService();

	protected CustomerService customerService = new CustomerService();

	protected CustomerPermissionService customerPermissionService = new CustomerPermissionService();

	protected CustomerStatisticPermissionService customerStatisticPermissionService = new CustomerStatisticPermissionService();

	protected ContactsService contactsService = new ContactsService();

	protected ContactsPermissionService contactsPermissionService = new ContactsPermissionService();

	protected OpportunityService opportunityService = new OpportunityService();

	protected OpportunityPermissionService opportunityPermissionService = new OpportunityPermissionService();

	protected OperationRecordService operationRecordService = new OperationRecordService();

	protected RecordService recordService = new RecordService();

	protected RecordPermissonService recordPermissonService = new RecordPermissonService();
	
	PermissionServiceBase permissionServiceBase = new PermissionServiceBase();

}
