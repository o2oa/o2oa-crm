package com.x.wcrm.assemble.control.jaxrs.contacts;

import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Contacts;

public class ActionListByCustomerIdPaging extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListByCustomerIdPaging.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement)
			throws Exception {
		logger.info("根据客户id，列出联系人列表");
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			String customerId = wi.getCustomerId();
			List<Contacts> os = business.contactsFactory().ListByCustomerId_Paging(customerId, adjustPage, adjustPageSize, wi.getOrderFieldName(),
					wi.getOrderType());
			List<Wo> wos = Wo.copier.copy(os);
			long count = business.contactsFactory().ListByCustomerId_Count(customerId);
			result.setData(wos);
			result.setCount(count);
			return result;
		}
	}

	public static class Wo extends Contacts {
		private static final long serialVersionUID = 1969250977615888280L;
		static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("客户的uuid(必填)")
		private String customerId;

		@FieldDescribe("排序字段名称")
		private String orderFieldName;

		@FieldDescribe("升序或者降序 desc或者asc")
		private String orderType;

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getOrderFieldName() {
			return orderFieldName;
		}

		public void setOrderFieldName(String orderFieldName) {
			this.orderFieldName = orderFieldName;
		}

		public String getOrderType() {
			return orderType;
		}

		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}

	}
}
