package com.x.wcrm.assemble.control.jaxrs.opportunity;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunity;
import com.x.wcrm.core.entity.Opportunity;

public class ActionGet extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(ActionGet.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		logger.info("商机 get");
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			Opportunity o = emc.find(id, Opportunity.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Opportunity.class);
			}
			//Wo wo = Wo.copier.copy(o);
			WrapOutOpportunity wo = Wo.copier_to_WrapOutOpportunity.copy(o);

			wo = opportunityService.supplementWrapOutOpportunity(business, wo);
			Wo result_wo = Wo.copier_to_Wo.copy(wo);
			// 联系人的操作记录。
			//result.setData(wo);
			result.setData(result_wo);
			return result;
		}

	}

	public static class Wo extends WrapOutOpportunity {
		private static final long serialVersionUID = 5661133561098715100L;
		//public static WrapCopier<Opportunity, Wo> copier = WrapCopierFactory.wo(Opportunity.class, Wo.class, null, JpaObject.FieldsInvisible);

		public static WrapCopier<Opportunity, WrapOutOpportunity> copier_to_WrapOutOpportunity = WrapCopierFactory.wo(Opportunity.class,
				WrapOutOpportunity.class, null, JpaObject.FieldsInvisible);
		public static WrapCopier<WrapOutOpportunity, Wo> copier_to_Wo = WrapCopierFactory.wo(WrapOutOpportunity.class, Wo.class, null,
				JpaObject.FieldsInvisible);
	}
}
