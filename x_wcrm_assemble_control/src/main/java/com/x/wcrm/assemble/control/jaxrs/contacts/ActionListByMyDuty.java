package com.x.wcrm.assemble.control.jaxrs.contacts;

import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.wrapin.ListPagingWi;
import com.x.wcrm.assemble.control.wrapout.WrapOutContacts;
import com.x.wcrm.core.entity.Contacts;

public class ActionListByMyDuty extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListByMyDuty.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement) throws Exception {
		logger.info("所有我负责的联系人。");
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			List<Contacts> os = contactsPermissionService.getList_MyDuty(ThisApplication.context(), business, effectivePerson, adjustPage,
					adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType());

			List<WrapOutContacts> wrapOutContactsList = Wo.copier_to_WrapOut.copy(os);
			wrapOutContactsList = contactsService.supplementWrapOutContactsList(business, wrapOutContactsList);
			List<Wo> wos = Wo.copier_to_Wo.copy(wrapOutContactsList);
			long count = contactsPermissionService.getList_MyDuty_Count(ThisApplication.context(), business, effectivePerson, wi.getKey());
			result.setCount(count);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends WrapOutContacts {
		/**
		 *
		 */
		private static final long serialVersionUID = 3592777709689395506L;
		//static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible, false);

		public static WrapCopier<Contacts, WrapOutContacts> copier_to_WrapOut = WrapCopierFactory.wo(Contacts.class, WrapOutContacts.class, null,
				JpaObject.FieldsInvisible);
		public static WrapCopier<WrapOutContacts, Wo> copier_to_Wo = WrapCopierFactory.wo(WrapOutContacts.class, Wo.class, null, JpaObject.FieldsInvisible);
	}
	public static class Wi extends ListPagingWi {

	}

}
