package com.x.wcrm.assemble.control.jaxrs.opportunity;

import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Opportunity;

//为商机对象增加 读者，作者
public class ActionRelevantPerson extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(ActionRelevantPerson.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement, String w_r_symbol) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。
			//
			Opportunity o = emc.find(id, Opportunity.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Opportunity.class);
			}
			logger.info(wi.toString());
			o = opportunityService.AddRelevantPerson(emc, business, o, wi.getPersonList(), w_r_symbol);
			// o = opportunityService.AddRelevantPerson(o, null, w_r_symbol);
			emc.beginTransaction(Opportunity.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();

			logger.info(o.toString());

			Wo wo = new Wo();
			wo = Wo.copier.copy(o);
			result.setData(wo);
			return result;
		}
	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("人员列表，distinguishname")
		List<String> personList;

		public List<String> getPersonList() {
			return personList;
		}

		public void setPersonList(List<String> personList) {
			this.personList = personList;
		}

	}

	public static class Wo extends Opportunity {
		private static final long serialVersionUID = -7207872030774397470L;
		static WrapCopier<Opportunity, Wo> copier = WrapCopierFactory.wo(Opportunity.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

}
