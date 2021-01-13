package com.x.wcrm.assemble.control.jaxrs.opportunity;

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
import com.x.wcrm.core.entity.Opportunity;

public class ActionUpdate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionUpdate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);

			if (StringUtils.isEmpty(id) || StringUtils.isBlank(id)) {
				throw new ExceptionOpportunityBaseMessage("商机id为空");
			} else {
				if (!business.opportunityFactory().IsExistById(id)) {
					throw new ExceptionEntityNotExist(id, Opportunity.class);
				}
			}

			Opportunity o = emc.find(id, Opportunity.class);
			String _id = o.getId();
			// 更新数据

			Wi.copier.copy(wi, o);
			o = opportunityService.initDefaulVal(o, effectivePerson);
			if (null == o.getId() || StringUtils.isBlank(o.getId())) {
				logger.info("ActionUpdate set id:" + _id);
				o.setId(_id);
			}

			emc.beginTransaction(Opportunity.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = new Wo();
			wo.setId(o.getId());
			result.setData(wo);
			return result;
		}
	}

	static class Wi extends Opportunity {
		/**
		 *
		 */
		private static final long serialVersionUID = -1558594136729236127L;
		static WrapCopier<Wi, Opportunity> copier = WrapCopierFactory.wi(Wi.class, Opportunity.class, null, JpaObject.FieldsUnmodify, true);
	}

	public static class Wo extends Opportunity {
		/**
		 *
		 */
		private static final long serialVersionUID = 2851607658582840721L;
		static WrapCopier<Opportunity, Wo> copier = WrapCopierFactory.wo(Opportunity.class, Wo.class, null, JpaObject.FieldsUnmodify, false);
	}
}
