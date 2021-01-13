package com.x.wcrm.assemble.control.jaxrs.opportunitytype;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.OpportunityType;

public class ActionCreate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。
			OpportunityType o = Wi.copier.copy(wi);

			// o = recordService.initDefaultValue(effectivePerson, o);
			emc.beginTransaction(OpportunityType.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends OpportunityType {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4754995631764586478L;
		static WrapCopier<Wi, OpportunityType> copier = WrapCopierFactory.wi(Wi.class, OpportunityType.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends WoId {
		static WrapCopier<OpportunityType, Wo> copier = WrapCopierFactory.wo(OpportunityType.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
