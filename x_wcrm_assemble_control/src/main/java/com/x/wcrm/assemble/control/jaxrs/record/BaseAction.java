package com.x.wcrm.assemble.control.jaxrs.record;

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
import com.x.wcrm.assemble.control.service.RecordService;

public class BaseAction extends StandardJaxrsAction {
	private final Logger logger = LoggerFactory.getLogger(BaseAction.class);

//	public static final String FOLLOWED = "已跟进";
//	public static final String UNFOLLOW = "未跟进";
//	public static final String WARITFORFOLLOW = "待跟进";

	protected List<String> convertToPerson(Business business, List<String> list) throws Exception {
		List<String> os = new ArrayList<>();
		DistinguishedNameCategory category = OrganizationDefinition.distinguishedNameCategory(list);
		os.addAll(business.organization().person().list(category.getPersonList()));
		os.addAll(business.organization().person().listWithIdentity(category.getIdentityList()));
		os.addAll(business.organization().person().listWithUnitSubDirect(category.getUnitList()));
		os = ListTools.trim(os, true, true);
		return os;
	}
	//
	//	static void initDefaultValue(EffectivePerson effectivePerson, Record record) {
	//		if (null == record.getCreateuser() || StringUtils.isBlank(record.getCreateuser())) {
	//			// 补全默认创建人
	//			record.setCreateuser(effectivePerson.getDistinguishedName());
	//		}
	//
	//	}

	RecordService recordService = new RecordService();

	LeadsService leadsService = new LeadsService();

	CustomerService customerService = new CustomerService();

	ContactsService contactsService = new ContactsService();

}
