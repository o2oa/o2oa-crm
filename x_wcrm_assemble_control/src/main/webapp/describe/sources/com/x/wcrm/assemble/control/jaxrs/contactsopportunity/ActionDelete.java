package com.x.wcrm.assemble.control.jaxrs.contactsopportunity;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckRemoveType;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.WrapOutId;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.ContactsAndOpportunity;

public class ActionDelete extends BaseAction {

	ActionResult<WrapOutId> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<WrapOutId> result = new ActionResult<>();
			Business business = new Business(emc);

			ContactsAndOpportunity o = emc.find(id, ContactsAndOpportunity.class);

			if (null == o) {
				throw new ExceptionEntityNotExist(id, ContactsAndOpportunity.class);
			}
			emc.beginTransaction(ContactsAndOpportunity.class);
			emc.remove(o, CheckRemoveType.all);
			emc.commit();
			WrapOutId wo = new WrapOutId(o.getId());
			result.setData(wo);
			return result;
		}
	}

}
