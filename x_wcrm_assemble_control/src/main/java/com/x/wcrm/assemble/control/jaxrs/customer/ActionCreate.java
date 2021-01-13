package com.x.wcrm.assemble.control.jaxrs.customer;

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
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Customer;

public class ActionCreate extends BaseAction {

	//	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			//Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。
			Customer o = Wi.copier.copy(wi);
			// 初始化默认值
			// ActionCreate.initDefaultValue(effectivePerson, o);
			o = customerService.initDefaulVal(o, effectivePerson);
			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.CREATE.VAL());

			return result;
		}
	}

	static class Wi extends Customer {
		private static final long serialVersionUID = 2868540251096117981L;
		static WrapCopier<Wi, Customer> copier = WrapCopierFactory.wi(Wi.class, Customer.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends WoId {
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

}
