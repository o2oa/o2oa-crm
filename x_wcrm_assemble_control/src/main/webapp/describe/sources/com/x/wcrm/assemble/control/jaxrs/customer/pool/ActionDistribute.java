package com.x.wcrm.assemble.control.jaxrs.customer.pool;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.assemble.control.jaxrs.customer.BaseAction;
import com.x.wcrm.assemble.control.wrapout.WrapOutCustomer;
import com.x.wcrm.core.entity.Customer;

//分配用户
public class ActionDistribute extends BaseAction {
	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			//			Business business = new Business(emc);
			Customer o = emc.find(id, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Customer.class);
			}

			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);

			o = customerService.initDefaulVal(o, effectivePerson);
			o.setOwneruser(wi.getDistinguishName());
			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.ASSIGN.VAL(), wi.getDistinguishName());

			// 客户关联联系人。
			// 客户的跟进记录。
			//result.setData(wo);
			return result;
		}

	}

	static class Wo extends WrapOutCustomer {
		/**
		 *
		 */
		private static final long serialVersionUID = 971548097968623723L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

	static class Wi extends GsonPropertyObject {

		@FieldDescribe("负责人的distinguishName")
		private String distinguishName;

		public String getDistinguishName() {
			return distinguishName;
		}

		public void setDistinguishName(String distinguishName) {
			this.distinguishName = distinguishName;
		}

	}
}
