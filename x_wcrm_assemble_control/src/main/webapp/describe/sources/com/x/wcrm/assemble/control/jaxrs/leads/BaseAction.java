package com.x.wcrm.assemble.control.jaxrs.leads;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.organization.OrganizationDefinition;
import com.x.base.core.project.organization.OrganizationDefinition.DistinguishedNameCategory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.service.ContactsService;
import com.x.wcrm.assemble.control.service.CustomerPermissionService;
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.service.LeadsPermissionService;
import com.x.wcrm.assemble.control.service.LeadsService;
import com.x.wcrm.assemble.control.service.OperationRecordService;

public class BaseAction extends StandardJaxrsAction {

//	private final Logger logger = LoggerFactory.getLogger(BaseAction.class);

	/**
	 * 1已转化 0 未转化
	 * */
	public final static String NOT_TRANSFORM = "0"; 
	public final static String HAS_TRANSFORM = "1"; 
	
	protected List<String> convertToPerson(Business business, List<String> list) throws Exception {
		List<String> os = new ArrayList<>();
		DistinguishedNameCategory category = OrganizationDefinition.distinguishedNameCategory(list);
		os.addAll(business.organization().person().list(category.getPersonList()));
		os.addAll(business.organization().person().listWithIdentity(category.getIdentityList()));
		os.addAll(business.organization().person().listWithUnitSubDirect(category.getUnitList()));
		os = ListTools.trim(os, true, true);
		return os;
	}

	//	// 初始化信息
	//	static void initDefaultValue(EffectivePerson effectivePerson, Leads leads) {
	//		// 线索的是否转化标志。如果为空默认未转化
	//		if (StringUtils.isBlank(leads.getIstransform())) {
	//			// 0 未转化
	//			leads.setIstransform("0");
	//		}
	//
	//		if (StringUtils.isBlank(leads.getCreateuser())) {
	//			leads.setCreateuser(effectivePerson.getDistinguishedName());
	//		}
	//
	//		if (StringUtils.isBlank(leads.getOwneruser())) {
	//			leads.setOwneruser(effectivePerson.getDistinguishedName());
	//		}
	//	}

	protected LeadsService leadsService = new LeadsService();

	protected CustomerService customerService = new CustomerService();

	protected ContactsService contactsService = new ContactsService();

	protected OperationRecordService operationRecordService = new OperationRecordService();

	protected CustomerPermissionService customerPermissionService = new CustomerPermissionService();
	
	protected LeadsPermissionService leadsPermissionService = new LeadsPermissionService();
}
