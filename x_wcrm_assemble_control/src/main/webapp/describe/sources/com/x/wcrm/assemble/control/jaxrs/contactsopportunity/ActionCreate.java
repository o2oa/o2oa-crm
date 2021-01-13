package com.x.wcrm.assemble.control.jaxrs.contactsopportunity;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.ContactsAndOpportunity;
import com.x.wcrm.core.entity.Opportunity;

public class ActionCreate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。

			Contacts contacts = emc.find(wi.getContactsid(), Contacts.class);
			if (null == contacts) {
				throw new ExceptionEntityNotExist(wi.getContactsid(), Contacts.class);
			}

			Opportunity opportunity = emc.find(wi.getOpportunityid(), Opportunity.class);
			if (null == opportunity) {
				throw new ExceptionEntityNotExist(wi.getOpportunityid(), Opportunity.class);
			}

			ContactsAndOpportunity o = Wi.copier.copy(wi);

			emc.beginTransaction(ContactsAndOpportunity.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			//增加操作记录
			//operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.CREATE.VAL());
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends ContactsAndOpportunity {
		private static final long serialVersionUID = -2465073355822524713L;
		static WrapCopier<Wi, ContactsAndOpportunity> copier = WrapCopierFactory.wi(Wi.class, ContactsAndOpportunity.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends ContactsAndOpportunity {
		private static final long serialVersionUID = -5060569953612059377L;
		static WrapCopier<ContactsAndOpportunity, Wo> copier = WrapCopierFactory.wo(ContactsAndOpportunity.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
