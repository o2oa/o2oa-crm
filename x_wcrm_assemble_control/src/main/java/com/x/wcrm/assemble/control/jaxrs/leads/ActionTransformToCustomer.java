package com.x.wcrm.assemble.control.jaxrs.leads;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Leads;

public class ActionTransformToCustomer extends BaseAction {
	// 线索转客户
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ActionTransformToCustomer.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
//			Business business = new Business(emc);
			Leads leads = emc.find(id, Leads.class);
			if (null == leads) {
				throw new ExceptionEntityNotExist(id, Leads.class);
			}

			Customer customer = new Customer();
			// 线索转客户
			customer = customerService.transformToCustomer(leads, customer);
			// 初始化customer的默认值
			customer = customerService.initDefaulVal(customer, effectivePerson);
			// 持久化客户数据

			emc.beginTransaction(Customer.class);
			emc.persist(customer, CheckPersistType.all);
			emc.commit();
			operationRecordService.SaveOperationRecord(emc, effectivePerson, customer, OperationRecordType.CREATE.VAL());
			
			// 更新线索数据，设置为已经转化状态。
			leads.setCustomerid(customer.getId());
			leads.setIstransform("1");

			emc.beginTransaction(Leads.class);
			emc.persist(leads, CheckPersistType.all);
			emc.commit();
			operationRecordService.SaveOperationRecord(emc, effectivePerson, leads, OperationRecordType.CHANGE_STATE.VAL(),"转化为客户");
			
			// 把转化后的线索和客户都输出。
			Wo wo = new Wo();
			wo.setCustomer(customer);
			wo.setLeads(leads);

			result.setData(wo);
			
			return result;
		}

	}

	public static class Wo extends GsonPropertyObject {
		Leads leads;
		Customer customer;

		public Leads getLeads() {
			return leads;
		}

		public void setLeads(Leads leads) {
			this.leads = leads;
		}

		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

	}

}
