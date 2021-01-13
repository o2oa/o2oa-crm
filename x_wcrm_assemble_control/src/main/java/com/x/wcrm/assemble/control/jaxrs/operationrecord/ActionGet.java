package com.x.wcrm.assemble.control.jaxrs.operationrecord;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutOperationRecord;
import com.x.wcrm.core.entity.OperationRecord;

class ActionGet extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			OperationRecord o = emc.find(id, OperationRecord.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, OperationRecord.class);
			}
			Wo wo = Wo.copier.copy(o);
			// 联系人关联关联客户。
			// 联系人的操作记录。
			result.setData(wo);
			return result;
		}

	}

	public static class Wo extends WrapOutOperationRecord {
		private static final long serialVersionUID = 5661133561098715100L;
		public static WrapCopier<OperationRecord, Wo> copier = WrapCopierFactory.wo(OperationRecord.class, Wo.class, null, JpaObject.FieldsInvisible);
	}
}
