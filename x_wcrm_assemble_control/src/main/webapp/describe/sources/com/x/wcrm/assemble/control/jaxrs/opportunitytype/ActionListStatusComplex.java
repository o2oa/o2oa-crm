package com.x.wcrm.assemble.control.jaxrs.opportunitytype;

import java.util.ArrayList;
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
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunityType;
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityType;

//根据商机状态组id，查询商机状态组，商机状态的组合信息
class ActionListStatusComplex extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListStatusComplex.class);

	// 先取出所有id，根据id列出所有线索
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<OpportunityType> os = business.opportunityTypeFactory().fetchAll(); // 创建时间倒序排列

			List<Wo> wos = Wo.copier.copy(os);

			for (Wo wo : wos) {
				List<OpportunityStatus> _list = business.opportunityStatusFactory().ListByTypeIdOrderByOrderId_Asc(wo.getId());
				wo.setOpportunityStatusList(_list);
			}
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends WrapOutOpportunityType {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6342914973024276186L;

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
