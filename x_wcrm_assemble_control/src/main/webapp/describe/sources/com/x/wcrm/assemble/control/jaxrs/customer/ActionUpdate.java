package com.x.wcrm.assemble.control.jaxrs.customer;

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
import com.x.wcrm.core.entity.Customer;

public class ActionUpdate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionUpdate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class); //把json数据解析转成wi实体
			Business business = new Business(emc);

			if (StringUtils.isEmpty(id) || StringUtils.isBlank(id)) {
				throw new ExceptionCustomerBaseMessage("客户id为空");
			} else {
				if (!business.customerFactory().IsExistById(id)) {
					throw new ExceptionEntityNotExist(id, Customer.class);
				}
			}

			Customer o = emc.find(id, Customer.class);//通过id获取客户信息
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

			Wi.copier.copy(wi, o); //覆盖内容
			o = customerService.initDefaulVal(o, effectivePerson); //写入一些额外的值，比如：是否锁定、是否成交
			if (null == o.getId() || StringUtils.isBlank(o.getId())) {
				logger.info("ActionUpdate set id:" + _id);
				o.setId(_id);
			}

			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			Wo.copier.copy(o,wo);
			wo.setId(o.getId());
			result.setData(wo);
            System.out.println("客户result=="+result);
			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.MODIFYVAL.VAL());

			return result;
		}
	}

	static class Wi extends Customer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 896057502019990501L;
		static WrapCopier<Wi, Customer> copier = WrapCopierFactory.wi(Wi.class, Customer.class, null, JpaObject.FieldsUnmodify, true);
	}

	public static class Wo extends Customer {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5187365424336678840L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsUnmodify, false);
	}
}
