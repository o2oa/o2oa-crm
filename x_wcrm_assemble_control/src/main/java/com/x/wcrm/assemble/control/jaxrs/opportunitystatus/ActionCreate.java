package com.x.wcrm.assemble.control.jaxrs.opportunitystatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.OpportunityStatus;

public class ActionCreate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。

			// 判断状态组id
			if (null == wi.getTypeid() || StringUtils.isBlank(wi.getTypeid())) {
				throw new ExceptionOpportunityTypeIdNull();
			} else {
				if (!business.opportunityTypeFactory().IsOpportunityTypeAvailable(wi.getTypeid())) {
					throw new ExceptionOpportunityTypeNotAvailable();
				}
			}

			String _typeid = wi.getTypeid();
			// 判断排序号

			if (null == wi.getOrderid()) {
				throw new ExceptionOpportunityStatusCommon("排序号不能为空.");
			} else {
				Integer _orderId = wi.getOrderid();
				if (_orderId < 0) {
					throw new ExceptionOpportunityStatusCommon("排序号大于或等于零的整数.");
				}

				if (!business.opportunityStatusFactory().IsAvailable_OpportunityStatus_OrderId_OpportunityType(_typeid, _orderId)) {
					throw new ExceptionOpportunityStatusCommon("商机组：" + _typeid + "中，排序号：" + _orderId + "已被使用");
				}
			}

			OpportunityStatus o = Wi.copier.copy(wi);

			// o = recordService.initDefaultValue(effectivePerson, o);
			emc.beginTransaction(OpportunityStatus.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends OpportunityStatus {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4754995631764586478L;
		static WrapCopier<Wi, OpportunityStatus> copier = WrapCopierFactory.wi(Wi.class, OpportunityStatus.class, null, JpaObject.FieldsUnmodify);

	}

	static class Wo extends WoId {
		static WrapCopier<OpportunityStatus, Wo> copier = WrapCopierFactory.wo(OpportunityStatus.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
