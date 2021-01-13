package com.x.wcrm.assemble.control.jaxrs.contacts;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;

public class ActionCreate extends BaseAction {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。

			String _customerid = wi.getCustomerid();
			if (StringUtils.isEmpty(_customerid) || StringUtils.isBlank(_customerid)) {
				throw new ExceptionContactsBaseMessage("客户id为空");
			} else {
				if (!business.customerFactory().IsExistById(_customerid)) {
					throw new ExceptionEntityNotExist(_customerid, Customer.class);
				}
			}

			Contacts o = Wi.copier.copy(wi);
			// 初始化默认值
			// ActionCreate.initDefaultValue(effectivePerson, o);
			contactsService.initDefaultValue(effectivePerson, o);

			emc.beginTransaction(Contacts.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			//增加操作记录
			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.CREATE.VAL());
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends Contacts {
		private static final long serialVersionUID = 2868540251096117981L;
		static WrapCopier<Wi, Contacts> copier = WrapCopierFactory.wi(Wi.class, Contacts.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends WoId {
		static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
