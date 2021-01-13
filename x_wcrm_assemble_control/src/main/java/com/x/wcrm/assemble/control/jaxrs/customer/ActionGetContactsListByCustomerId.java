package com.x.wcrm.assemble.control.jaxrs.customer;

import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutContacts;
import com.x.wcrm.core.entity.Contacts;

class ActionGetContactsListByCustomerId extends BaseAction {

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String customerId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Contacts> os = business.contactsFactory().ListByCustomerId(customerId);
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			return result;
		}

	}

	public static class Wo extends WrapOutContacts {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9200615663743163973L;
		public static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, null, false);
	}
}
