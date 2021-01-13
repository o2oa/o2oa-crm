package com.x.wcrm.assemble.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.project.http.EffectivePerson;
import com.x.organization.core.express.Organization;
import com.x.organization.core.express.person.PersonFactory;
import com.x.wcrm.assemble.control.factory.AttachmentFactory;
import com.x.wcrm.assemble.control.factory.ContactsAndOpportunityFactory;
import com.x.wcrm.assemble.control.factory.ContactsFactory;
import com.x.wcrm.assemble.control.factory.ContactsStatisticFactory;
import com.x.wcrm.assemble.control.factory.CrmPersonFactory;
import com.x.wcrm.assemble.control.factory.CustomerFactory;
import com.x.wcrm.assemble.control.factory.CustomerStatisticFactory;
import com.x.wcrm.assemble.control.factory.LeadsFactory;
import com.x.wcrm.assemble.control.factory.OperationRecordFactory;
import com.x.wcrm.assemble.control.factory.OpportunityFactory;
import com.x.wcrm.assemble.control.factory.OpportunityStatusFactory;
import com.x.wcrm.assemble.control.factory.OpportunityTypeFactory;
import com.x.wcrm.assemble.control.factory.RecordFactory;

public class Business {

	private EntityManagerContainer emc;

	public Business(EntityManagerContainer emc) throws Exception {
		this.emc = emc;
	}

	public EntityManagerContainer entityManagerContainer() {
		return this.emc;
	}

	private PersonFactory personFactory;

	public PersonFactory personFactory() throws Exception {
		if (null == this.personFactory) {
			this.personFactory = new PersonFactory(ThisApplication.context());
		}
		return personFactory;
	}

	// private UnitFactory unitFactory;
	//
	// public UnitFactory unitFactory() throws Exception {
	// if (null == this.unitFactory) {
	// this.unitFactory = new UnitFactory(ThisApplication.context());
	// }
	// return unitFactory;
	// }
	//
	// private GroupFactory groupFactory;
	//
	// public GroupFactory groupFactory() throws Exception {
	// if (null == this.groupFactory) {
	// this.groupFactory = new GroupFactory(ThisApplication.context());
	// }
	// return groupFactory;
	// }
	//
	//
	// private PersonAttributeFactory personAttribute;
	//
	// public PersonAttributeFactory personAttributeFactory() throws Exception {
	// if (null == this.personAttribute) {
	// this.personAttribute = new PersonAttributeFactory(ThisApplication.context());
	// }
	// return personAttribute;
	// }
	//
	// private IdentityFactory identityFactory;
	//
	// public IdentityFactory identityFactory() throws Exception {
	// if (null == this.identityFactory) {
	// this.identityFactory = new IdentityFactory(ThisApplication.context());
	// }
	// return identityFactory;
	// }

	private Organization organization; // o2组织
	private LeadsFactory leadsFactory; // 线索
	private CustomerFactory customerFactory; // 客户
	private ContactsFactory contactsFactory; // 联系人
	private ContactsAndOpportunityFactory contactsAndOpportunityFactory; // 联系人商机关系
	private OpportunityFactory opportunityFactory; // 商机

	private OpportunityTypeFactory opportunityTypeFactory; // 商机状态组
	private OpportunityStatusFactory opportunityStatusFactory; // 商机状态；

	private RecordFactory recordFactory; // 跟进记录

	private OperationRecordFactory operationRecordFactory; // 操作记录

	private AttachmentFactory attachmentFactory; // CRM附件

	private CustomerStatisticFactory customerStatisticFactory; // 客户统计

	private ContactsStatisticFactory contactsStatisticFactory; // 联系人统计

	public Organization organization() throws Exception {
		if (null == this.organization) {
			this.organization = new Organization(ThisApplication.context());
		}
		return organization;
	}

	public LeadsFactory leadsFactory() throws Exception {
		if (null == this.leadsFactory) {
			this.leadsFactory = new LeadsFactory(this);
		}
		return leadsFactory;
	}

	public CustomerFactory customerFactory() throws Exception {
		if (null == this.customerFactory) {
			this.customerFactory = new CustomerFactory(this);
		}
		return customerFactory;
	}

	public CustomerStatisticFactory customerStatisticFactory() throws Exception {
		if (null == this.customerStatisticFactory) {
			this.customerStatisticFactory = new CustomerStatisticFactory(this);
		}
		return customerStatisticFactory;
	}

	public ContactsFactory contactsFactory() throws Exception {
		if (null == this.contactsFactory) {
			this.contactsFactory = new ContactsFactory(this);
		}
		return contactsFactory;
	}

	public ContactsStatisticFactory contactsStatisticFactory() throws Exception {
		if (null == this.contactsStatisticFactory) {
			this.contactsStatisticFactory = new ContactsStatisticFactory(this);
		}
		return contactsStatisticFactory;
	}

	public ContactsAndOpportunityFactory contactsAndOpportunityFactory() throws Exception {
		if (null == this.contactsAndOpportunityFactory) {
			this.contactsAndOpportunityFactory = new ContactsAndOpportunityFactory(this);
		}
		return contactsAndOpportunityFactory;
	}

	public OpportunityFactory opportunityFactory() throws Exception {
		if (null == this.opportunityFactory) {
			this.opportunityFactory = new OpportunityFactory(this);
		}
		return opportunityFactory;
	}

	public OpportunityTypeFactory opportunityTypeFactory() throws Exception {
		if (null == this.opportunityTypeFactory) {
			this.opportunityTypeFactory = new OpportunityTypeFactory(this);
		}
		return opportunityTypeFactory;
	}

	public OpportunityStatusFactory opportunityStatusFactory() throws Exception {
		if (null == this.opportunityStatusFactory) {
			this.opportunityStatusFactory = new OpportunityStatusFactory(this);
		}
		return opportunityStatusFactory;
	}

	public RecordFactory recordFactory() throws Exception {
		if (null == this.recordFactory) {
			this.recordFactory = new RecordFactory(this);
		}
		return recordFactory;
	}

	public OperationRecordFactory operationRecordFactory() throws Exception {
		if (null == this.operationRecordFactory) {
			this.operationRecordFactory = new OperationRecordFactory(this);
		}
		return operationRecordFactory;
	}

	public AttachmentFactory attachmentFactory() throws Exception {
		if (null == this.attachmentFactory) {
			this.attachmentFactory = new AttachmentFactory(this);
		}
		return attachmentFactory;
	}

	private CrmPersonFactory crmPersonFactory;

	public CrmPersonFactory crmPersonFactory() throws Exception {
		if (null == this.crmPersonFactory) {
			this.crmPersonFactory = new CrmPersonFactory(this);
		}
		return crmPersonFactory;
	}

	public boolean isHasPlatformRole(String personName, String roleName) throws Exception {
		if (StringUtils.isEmpty(personName)) {
			throw new Exception("personName is null!");
		}
		if (StringUtils.isEmpty(roleName)) {
			throw new Exception("roleName is null!");
		}
		List<String> roleList = null;
		roleList = organization().role().listWithPerson(personName);
		if (roleList != null && !roleList.isEmpty()) {
			if (roleList.stream().filter(r -> roleName.equalsIgnoreCase(r)).count() > 0) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * TODO 判断用户是否管理员权限 1、person.isManager() 2、xadmin 3、CRMManager
	 * 
	 * @param request
	 * @param personName
	 * @return
	 * @throws Exception
	 */
	public boolean isManager(HttpServletRequest request, EffectivePerson person) throws Exception {
		// 如果用户的身份是平台的超级管理员，那么就是超级管理员权限
		if (person.isManager()) {
			return true;
		}
		if ("xadmin".equalsIgnoreCase(person.getDistinguishedName())) {
			return true;
		}
		if (isHasPlatformRole(person.getDistinguishedName(), ThisApplication.ROLE_CRMManager)) {
			return true;
		}
		return false;
	}

	/**
	 * TODO 判断用户是否管理员权限 1、person.isManager() 2、xadmin 3、CRMManager
	 * 
	 * @param request
	 * @param personName
	 * @return
	 * @throws Exception
	 */

	public boolean isManager(EffectivePerson person) throws Exception {
		// 如果用户的身份是平台的超级管理员，那么就是超级管理员权限
		if (person.isManager()) {
			return true;
		}
		if ("xadmin".equalsIgnoreCase(person.getDistinguishedName())) {
			return true;
		}
		if (isHasPlatformRole(person.getDistinguishedName(), ThisApplication.ROLE_CRMManager)) {
			return true;
		}
		return false;
	}

}
