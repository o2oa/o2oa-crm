package com.x.wcrm.assemble.control.jaxrs.contactsopportunity;

import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.ContactsAndOpportunity;

public class ActionListContactsByOpportunityId extends BaseAction {
	//	private static Logger logger = LoggerFactory.getLogger(ActionListContactsByOpportunityId.class);

	// 根据商机uuid列出关联的联系人列表(创建时间倒序列)
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String opportunityid) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<String> idList = business.contactsAndOpportunityFactory().ListContactsIds_ByOpportunityId(opportunityid); // 关联关系创建时间倒序排列
			List<Contacts> os = business.contactsFactory().ListByUUIDList(idList); //无序

			List<Wo> wos = Wo.copier.copy(os);
			for (Wo wo : wos) {
				List<ContactsAndOpportunity> contactsAndOpportunityList = business.contactsAndOpportunityFactory()
						.getOne_By_OpportunityId_And_ContactsId(opportunityid, wo.getId());
				wo.setContactsAndOpportunity(contactsAndOpportunityList);
			}

			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Contacts {
		private static final long serialVersionUID = 8664968335850421226L;
		static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible);

		private List<ContactsAndOpportunity> contactsAndOpportunity;

		public List<ContactsAndOpportunity> getContactsAndOpportunity() {
			return contactsAndOpportunity;
		}

		public void setContactsAndOpportunity(List<ContactsAndOpportunity> contactsAndOpportunity) {
			this.contactsAndOpportunity = contactsAndOpportunity;
		}

	}
}
