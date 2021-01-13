package com.x.wcrm.assemble.control.jaxrs.customer;

import static com.x.wcrm.assemble.control.service.CustomerService.UNLOCK;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Customer;

public class ActionUnLock extends BaseAction {

	//	private static Logger logger = LoggerFactory.getLogger(ActionUnLock.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String customerid) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

			//			Business business = new Business(emc);
			Customer o = emc.find(customerid, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(customerid, Customer.class);
			}

			//这里其实要判断权限
			o.setIslock(UNLOCK);

			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = Wo.copier.copy(o);
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.UNLOCK.VAL());
			return result;
		}
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 2207385950874880375L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}
}
