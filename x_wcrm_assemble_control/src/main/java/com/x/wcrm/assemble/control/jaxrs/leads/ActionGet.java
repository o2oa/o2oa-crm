package com.x.wcrm.assemble.control.jaxrs.leads;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutLeads;
import com.x.wcrm.core.entity.Leads;

class ActionGet extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			Leads leads = emc.find(id, Leads.class);
			if (null == leads) {
				throw new ExceptionEntityNotExist(id, Leads.class);
			}
			Wo wo = Wo.copier.copy(leads);

			// 线索转客户。
			// 线索的跟进记录。
			result.setData(wo);
			return result;
		}

	}

	public static class Wo extends WrapOutLeads {
		private static final long serialVersionUID = 5661133561098715100L;
		public static WrapCopier<Leads, Wo> copier = WrapCopierFactory.wo(Leads.class, Wo.class, null,
				JpaObject.FieldsInvisible);
	}
}
