package com.x.wcrm.assemble.control.jaxrs.contacts;

import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
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
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Contacts;

public class ActionUpdate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionUpdate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);

			if (StringUtils.isEmpty(wi.getCustomerid()) || StringUtils.isBlank(wi.getCustomerid())) {
				throw new ExceptionContactsBaseMessage("关联的客户id为空");
			} else {
				if (!business.customerFactory().IsExistById(wi.getCustomerid())) {
					throw new ExceptionEntityNotExist(wi.getCustomerid(), Contacts.class);
				}
			}

			Contacts o = emc.find(id, Contacts.class);
			String _id = o.getId();
			// 更新数据

			//			if (isKeepOriginalData) {
			//				// 如果update的数据项为空，那个保留原对象的数据。
			//				Wi.copier.copy(wi, o);
			//				o = customerService.initDefaulVal(o, effectivePerson);
			//			} else {
			//				// 使用update的数据完全覆盖原对象数据
			//				Wi.copier.copy(wi, o);
			//				o = customerService.initDefaulVal(o, effectivePerson);
			//
			//			}

			Wi.copier.copy(wi, o);
			o = contactsService.initDefaultValue(effectivePerson, o);
			if (null == o.getId() || StringUtils.isBlank(o.getId())) {
				logger.info("ActionUpdate set id:" + _id);
				o.setId(_id);
			}

			emc.beginTransaction(Contacts.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.MODIFYVAL.VAL());
			return result;
		}
	}

	static class Wi extends Contacts {
		/**
		 *
		 */
		private static final long serialVersionUID = 69956572097541525L;
		static WrapCopier<Wi, Contacts> copier = WrapCopierFactory.wi(Wi.class, Contacts.class, null, JpaObject.FieldsUnmodify, true);
	}

	public static class Wo extends Contacts {
		/**
		 *
		 */
		private static final long serialVersionUID = -4469659289755872265L;
		static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsUnmodify, false);
	}
}
