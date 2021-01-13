package com.x.wcrm.assemble.control.jaxrs.opportunity;

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
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Opportunity;

public class ActionCreate extends BaseAction {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。
			Opportunity o = Wi.copier.copy(wi);
			// 初始化默认值
			// ActionCreate.initDefaultValue(effectivePerson, o);
			// contactsService.initDefaultValue(effectivePerson, o);
			o = opportunityService.initDefaulVal(o, effectivePerson);
			emc.beginTransaction(Opportunity.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			//增加操作记录
			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.CREATE.VAL());
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends Opportunity {
		private static final long serialVersionUID = 2868540251096117981L;
		static WrapCopier<Wi, Opportunity> copier = WrapCopierFactory.wi(Wi.class, Opportunity.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends WoId {
		static WrapCopier<Opportunity, Wo> copier = WrapCopierFactory.wo(Opportunity.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
