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
import com.x.wcrm.core.entity.Customer;

class ActionList extends BaseAction {

	//	private static Logger logger = LoggerFactory.getLogger(ActionList.class);

	// 先取出所有id，根据id列出所有线索
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<String> ids = business.customerFactory().fetchAllIds();
			List<Wo> wos = Wo.copier.copy(emc.list(Customer.class, ids));
			result.setData(wos);
			return result;
		}
	}

	// 列出创建是当前登录用户的所有线索,并且按照创建时间倒序排列。
	ActionResult<List<Wo>> MyExecute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<String> ids = business.customerFactory().fetchAllIdsByCreator(effectivePerson.getDistinguishedName());
			List<Wo> wos = Wo.copier.copy(emc.list(Customer.class, ids));
			result.setData(wos);
			return result;
		}
	}

	// 所有线索，按照时间倒序排列。
	ActionResult<List<Wo>> Execute_OrderByCreateTime(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Customer> os = business.customerFactory().fetchAll();
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

}
