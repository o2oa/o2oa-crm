package com.x.wcrm.assemble.control.jaxrs.customer;

import static com.x.wcrm.assemble.control.service.CustomerService.LOCK;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Customer;

public class ActionLock extends BaseAction {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ActionLock.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String customerid) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

			//Business business = new Business(emc);
			Customer o = emc.find(customerid, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(customerid, Customer.class);
			}

			//这里其实要判断权限
			o.setIslock(LOCK);

			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = Wo.copier.copy(o);
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.LOCK.VAL());
			return result;
		}
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 2386512800512857790L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}
}
