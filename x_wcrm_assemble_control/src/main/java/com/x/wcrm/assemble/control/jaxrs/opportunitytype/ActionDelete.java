package com.x.wcrm.assemble.control.jaxrs.opportunitytype;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckRemoveType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunityType;
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityType;

//删除商机状态组，并且关联删除该组下的商机状态
public class ActionDelete extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			//			if (!business.buildingEditAvailable(effectivePerson)) {
			//				throw new ExceptionAccessDenied(effectivePerson);
			//			}
			OpportunityType opportunityType = emc.find(id, OpportunityType.class);
			if (null == opportunityType) {
				throw new ExceptionEntityNotExist(id, OpportunityType.class);
			}
			Wo wo = Wo.copier.copy(opportunityType);

			emc.beginTransaction(OpportunityType.class);
			emc.beginTransaction(OpportunityStatus.class);

			List<OpportunityStatus> _list = business.opportunityStatusFactory().ListByTypeId(opportunityType.getId());
			wo.setOpportunityStatusList(_list);

			for (OpportunityStatus opportunityStatus : _list) {
				emc.remove(opportunityStatus);
			}

			emc.remove(opportunityType);
			emc.commit();

			result.setData(wo);
			return result;
		}
	}

	public static class Wo extends WrapOutOpportunityType {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7474919903216307298L;

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
