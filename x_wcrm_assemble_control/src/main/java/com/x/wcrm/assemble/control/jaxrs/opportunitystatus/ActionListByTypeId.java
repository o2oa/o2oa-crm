package com.x.wcrm.assemble.control.jaxrs.opportunitystatus;

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
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityType;

class ActionListByTypeId extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListByTypeId.class);

	// 根据商机状态组id列出所有关联的的商机状态(创建时间倒序列)
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String typeid) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			// List<OpportunityStatus> os = business.opportunityStatusFactory().ListByTypeId(typeid); // 创建时间倒序排列
			List<OpportunityStatus> os = business.opportunityStatusFactory().ListByTypeIdOrderByOrderId_Asc(typeid); // 排序号倒序排列

			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends OpportunityStatus {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4845577172188804104L;
		static WrapCopier<OpportunityStatus, Wo> copier = WrapCopierFactory.wo(OpportunityStatus.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
