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
import com.x.wcrm.core.entity.ContactsAndOpportunity;

public class ActionList extends BaseAction {

	//	private static Logger logger = LoggerFactory.getLogger(ActionList.class);

	// 所有线索，按照时间倒序排列。
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<ContactsAndOpportunity> os = business.contactsAndOpportunityFactory().fetchAll();
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends ContactsAndOpportunity {
		private static final long serialVersionUID = 4036281302566710351L;
		static WrapCopier<ContactsAndOpportunity, Wo> copier = WrapCopierFactory.wo(ContactsAndOpportunity.class, Wo.class, null,
				JpaObject.FieldsInvisible);
	}

}
