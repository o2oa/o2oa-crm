package com.x.wcrm.assemble.control.jaxrs.record;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutRecord;
import com.x.wcrm.core.entity.Record;;

class ActionGet extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			Record record = emc.find(id, Record.class);
			if (null == record) {
				throw new ExceptionEntityNotExist(id, Record.class);
			}
			Wo wo = Wo.copier.copy(record);
			// 跟进记录。
			result.setData(wo);
			return result;
		}

	}

	public static class Wo extends WrapOutRecord {
		private static final long serialVersionUID = 5661133561098715100L;
		public static WrapCopier<Record, Wo> copier = WrapCopierFactory.wo(Record.class, Wo.class, null, JpaObject.FieldsInvisible);
	}
}
