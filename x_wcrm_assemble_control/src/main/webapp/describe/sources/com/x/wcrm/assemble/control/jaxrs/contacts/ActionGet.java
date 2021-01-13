package com.x.wcrm.assemble.control.jaxrs.contacts;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutContacts;
import com.x.wcrm.core.entity.Contacts;

class ActionGet extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			Contacts o = emc.find(id, Contacts.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Contacts.class);
			}
			//Wo wo = Wo.copier.copy(o);

			WrapOutContacts wrapOutContacts = Wo.copier_to_WrapOut.copy(o);
			wrapOutContacts = contactsService.supplementWrapOutContacts(business, wrapOutContacts);
			Wo wo = Wo.copier_to_Wo.copy(wrapOutContacts);

			// 联系人关联关联客户。
			// 联系人的操作记录。
			result.setData(wo);
			return result;
		}

	}

	public static class Wo extends WrapOutContacts {
		private static final long serialVersionUID = 5661133561098715100L;
		//public static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible);
		static WrapCopier<Contacts, WrapOutContacts> copier_to_WrapOut = WrapCopierFactory.wo(Contacts.class, WrapOutContacts.class, null,JpaObject.FieldsInvisible);
		static WrapCopier<WrapOutContacts, Wo> copier_to_Wo = WrapCopierFactory.wo(WrapOutContacts.class, Wo.class, null, JpaObject.FieldsInvisible);

	}
}
