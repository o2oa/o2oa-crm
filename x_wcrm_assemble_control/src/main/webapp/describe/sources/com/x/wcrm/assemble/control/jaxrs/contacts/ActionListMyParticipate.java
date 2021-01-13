package com.x.wcrm.assemble.control.jaxrs.contacts;

import java.util.List;

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
import com.x.wcrm.assemble.control.wrapout.WrapOutContacts;
import com.x.wcrm.core.entity.Contacts;

public class ActionListMyParticipate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListMyParticipate.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson) throws Exception {
		logger.info("联系人：我参与的。");
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Contacts> os = contactsPermissionService.getList_MyParticipate(ThisApplication.context(), business, effectivePerson);
			//List<Wo> wos = Wo.copier.copy(os);
			List<WrapOutContacts> wrapOutContactsList = Wo.copier_to_WrapOut.copy(os);
			wrapOutContactsList = contactsService.supplementWrapOutContactsList(business, wrapOutContactsList);
			List<Wo> wos = Wo.copier_to_Wo.copy(wrapOutContactsList);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Contacts {
		/**
		 *
		 */
		private static final long serialVersionUID = 1026809902255407555L;
		//static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible, false);
		static WrapCopier<Contacts, WrapOutContacts> copier_to_WrapOut = WrapCopierFactory.wo(Contacts.class, WrapOutContacts.class, null,JpaObject.FieldsInvisible);
		static WrapCopier<WrapOutContacts, Wo> copier_to_Wo = WrapCopierFactory.wo(WrapOutContacts.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
