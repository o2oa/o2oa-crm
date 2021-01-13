package com.x.wcrm.assemble.control.jaxrs.opportunitytype;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunityType;
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityType;

class ActionGetComplex extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			OpportunityType o = emc.find(id, OpportunityType.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, OpportunityType.class);
			}
			Wo wo = Wo.copier.copy(o);
			List<OpportunityStatus> _list = business.opportunityStatusFactory().ListByTypeIdOrderByOrderId_Asc(wo.getId());
			wo.setOpportunityStatusList(_list);

			result.setData(wo);
			return result;
		}

	}

	public static class Wo extends WrapOutOpportunityType {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1493014664609012054L;

		static WrapCopier<OpportunityType, Wo> copier = WrapCopierFactory.wo(OpportunityType.class, Wo.class, null, JpaObject.FieldsInvisible);

		private List<OpportunityStatus> OpportunityStatusList = new ArrayList<OpportunityStatus>();

		public List<OpportunityStatus> getOpportunityStatusList() {
			return OpportunityStatusList;
		}

		public void setOpportunityStatusList(List<OpportunityStatus> opportunityStatusList) {
			OpportunityStatusList = opportunityStatusList;
		}

	}

}
