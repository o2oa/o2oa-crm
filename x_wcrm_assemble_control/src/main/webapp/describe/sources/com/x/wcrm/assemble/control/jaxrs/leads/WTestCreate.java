package com.x.wcrm.assemble.control.jaxrs.leads;

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

import com.x.wcrm.core.entity.WTest;

public class WTestCreate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(WTestCreate.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		logger.info("WTestCreate==x1");
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			logger.info("WTestCreate==x2");
			ActionResult<Wo> result = new ActionResult<>();
			logger.info("WTestCreate==x3");
			logger.info(jsonElement.getAsString());
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);

			logger.info("over convertToWrapIn!");

			Business business = new Business(emc);
			WTest leads = Wi.copier.copy(wi);
			emc.persist(leads, CheckPersistType.all);
			emc.commit();

			Wo wo = new Wo();
			logger.info("WTestCreate==>ActionCreate==>getId:" + leads.getId());
			wo.setId(leads.getId());
			result.setData(wo);
			return result;
		}
	}

	public static class Wi extends WTest {
		private static final long serialVersionUID = 1595428934626864861L;
		static WrapCopier<Wi, WTest> copier = WrapCopierFactory.wi(Wi.class, WTest.class, null,
				JpaObject.FieldsUnmodify);
	}

	public static class Wo extends WoId {
	}

}
