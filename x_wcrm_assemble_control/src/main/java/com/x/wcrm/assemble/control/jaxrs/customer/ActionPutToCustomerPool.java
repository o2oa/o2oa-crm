package com.x.wcrm.assemble.control.jaxrs.customer;

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
import org.apache.commons.lang3.StringUtils;

public class ActionPutToCustomerPool extends BaseAction {
	//	private static Logger logger = LoggerFactory.getLogger(ActionLock.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String customerid) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

			//			Business business = new Business(emc);
			Customer o = emc.find(customerid, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(customerid, Customer.class);
			}
			if(StringUtils.equals("1",o.getIslock())){
				//若为锁定状态则不允许放入公海
				throw new ExceptionCustomerBaseMessage("Id为："+customerid + "的客户为锁定状态，请先解除锁定！");
			}
			o.setOwneruser(""); //设置为null会导致展现时候字段为空（即使参数ignoreNull为false）

			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = Wo.copier.copy(o);
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.PUT_TO_OPENSEA.VAL());
			return result;
		}
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 3335679407122384087L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}
}
