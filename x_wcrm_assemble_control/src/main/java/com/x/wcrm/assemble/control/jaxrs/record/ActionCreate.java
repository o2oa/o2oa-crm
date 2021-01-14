package com.x.wcrm.assemble.control.jaxrs.record;

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
import com.x.wcrm.assemble.control.service.RecordService;
import com.x.wcrm.core.entity.Record;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class ActionCreate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。
			Record o = Wi.copier.copy(wi);

			o = recordService.initDefaultValue(effectivePerson, o);
			emc.beginTransaction(Record.class);
			o.setContent(wi.getContent());
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			
			//更新 客户或线索或商机或联系人的下次跟进时间（nexttime）
			recordService.updateNextTimeCRM(wi.getTypes(), wi.getTypesid(), wi.getNexttime());
			
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends Record {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7106839105196570589L;
		static WrapCopier<Wi, Record> copier = WrapCopierFactory.wi(Wi.class, Record.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends WoId {
		static WrapCopier<Record, Wo> copier = WrapCopierFactory.wo(Record.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
