package com.x.wcrm.assemble.control.jaxrs.leads;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Leads;

public class ActionUpdate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionUpdate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String leadsid, boolean isKeepOriginalData, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			//			Business business = new Business(emc);
			Leads o = emc.find(leadsid, Leads.class);
			String _id = o.getId();
			// 更新数据

			if (isKeepOriginalData) {
				// 如果update的数据项为空，那个保留原对象的数据。
				Wi.copier.copy(wi, o);
				o = leadsService.initDefaultValue(effectivePerson, o);
			} else {
				// 使用update的数据完全覆盖原对象数据
				Wi.copier.copy(wi, o);
				o = leadsService.initDefaultValue(effectivePerson, o);

			}
			if (null == o.getId() || StringUtils.isBlank(o.getId())) {
				logger.info("ActionUpdate set id:" + _id);
				o.setId(_id);
			}

			emc.beginTransaction(Leads.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.MODIFYVAL.VAL());

			Wo wo = new Wo();
			Wo.copier.copy(o,wo);
			wo.setId(o.getId());
			result.setData(wo);

			System.out.println("线索result=="+result);
			return result;
		}
	}

	static class Wi extends Leads {
		private static final long serialVersionUID = -4714395467753481398L;
		static WrapCopier<Wi, Leads> copier = WrapCopierFactory.wi(Wi.class, Leads.class, null, JpaObject.FieldsUnmodify, true);
	}

	public static class Wo extends Leads {
		private static final long serialVersionUID = 7871578639804765941L;
		static WrapCopier<Leads, Wo> copier = WrapCopierFactory.wo(Leads.class, Wo.class, null, JpaObject.FieldsUnmodify);
	}
}
