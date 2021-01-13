package com.x.wcrm.assemble.control.jaxrs.opportunity;

import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.wrapin.ListPagingWi;
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunity;
import com.x.wcrm.core.entity.Opportunity;

public class ActionListMyParticipate extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListMyParticipate.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement) throws Exception {
		logger.info("商机：我参与的。");
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<Opportunity> os = opportunityPermissionService.getList_MyParticipate(ThisApplication.context(), business, effectivePerson, adjustPage,
					adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType());
			List<WrapOutOpportunity> WrapOutOpportunityList = Wo.copier_to_WrapOut.copy(os);
			WrapOutOpportunityList = opportunityService.supplementWrapOutOpportunityList(business, WrapOutOpportunityList);
			List<Wo> wos = Wo.copier_to_Wo.copy(WrapOutOpportunityList);
			result.setData(wos);
			long count = opportunityPermissionService.getList_MyParticipate_Count(ThisApplication.context(), business, effectivePerson, wi.getKey());
			result.setCount(count);
			return result;
		}
	}

	public static class Wo extends WrapOutOpportunity {
		private static final long serialVersionUID = 5220686039082993620L;
		//static WrapCopier<Opportunity, Wo> copier = WrapCopierFactory.wo(Opportunity.class, Wo.class, null, JpaObject.FieldsInvisible, false);
		static WrapCopier<Opportunity, WrapOutOpportunity> copier_to_WrapOut = WrapCopierFactory.wo(Opportunity.class, WrapOutOpportunity.class, null,
				JpaObject.FieldsInvisible);
		static WrapCopier<WrapOutOpportunity, Wo> copier_to_Wo = WrapCopierFactory.wo(WrapOutOpportunity.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

	public static class Wi extends ListPagingWi {

	}
}
