package com.x.wcrm.assemble.control.jaxrs.customer;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;

//同时创建联系人
public class ActionCreateAndCreateContacts extends BaseAction {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ActionCreateAndCreateContacts.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

			//			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。

			Wi wi_customer = this.convertToWrapIn(jsonElement, Wi.class);
			Customer customer = Wi.copier.copy(wi_customer);
			// 初始化默认值
			// ActionCreate.initDefaultValue(effectivePerson, o);
			customer = customerService.initDefaulVal(customer, effectivePerson);
			emc.beginTransaction(Customer.class);
			emc.persist(customer, CheckPersistType.all);
			emc.commit();

			Contacts contacts = new Contacts();
			contacts = contactsService.createFromCustomer(effectivePerson, contacts, customer);
			// 初始化默认值
			contacts = contactsService.initDefaultValue(effectivePerson, contacts);
			emc.beginTransaction(Contacts.class);
			emc.persist(contacts, CheckPersistType.all);
			emc.commit();

			Wo wo = new Wo();
			wo.setCustomer(customer);
			wo.setContacts(contacts);
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends Customer {
		private static final long serialVersionUID = 2868540251096117981L;
		static WrapCopier<Wi, Customer> copier = WrapCopierFactory.wi(Wi.class, Customer.class, null, JpaObject.FieldsUnmodify);

	}

	//	static class Wo extends WoId {
	//		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible);
	//	}

	static class Wo extends GsonPropertyObject {
		Customer customer;
		Contacts contacts;

		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

		public Contacts getContacts() {
			return contacts;
		}

		public void setContacts(Contacts contacts) {
			this.contacts = contacts;
		}

	}

}
