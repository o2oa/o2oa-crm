package com.x.wcrm.assemble.control.jaxrs.customer;

import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.CustomerAndLastRecord;

class ActionATest extends BaseAction {

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String para) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			//List<String> os = business.customerFactory().List_gt_count(Integer.parseInt(para));
			//List<Customer> os = business.customerFactory().List_gt_count2(Integer.parseInt(para));
			
			//List<SimpleKV> os = business.customerFactory().List_gt_count2(Integer.parseInt(para));
			List<CustomerAndLastRecord> os = business.customerFactory().List_last_optionTime(para);
			
			List<Wo> wos = Wo.copier.copy(os);
			// 客户关联联系人。
			// 客户的跟进记录。
			result.setData(wos);
			return result;
		}

	}

	public static class Wo extends CustomerAndLastRecord {

		private long count;
		/**
		 * 
		 */
		private static final long serialVersionUID = 3737726852892756455L;
		//public static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, null, false);
		static WrapCopier<CustomerAndLastRecord, Wo> copier = WrapCopierFactory.wo(CustomerAndLastRecord.class, Wo.class, null, JpaObject.FieldsInvisible, false);

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}
	}
}
