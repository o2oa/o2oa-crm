package com.x.wcrm.assemble.control.jaxrs.contactsopportunity;

import java.util.List;

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
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunity;
import com.x.wcrm.core.entity.Opportunity;

public class ActionListOpportunityByContactsId extends BaseAction {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ActionListOpportunityByContactsId.class);

	// 根据联系人uuid列出关联的商机列表(创建时间倒序列)
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String contactsid) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);

			List<String> idList = business.contactsAndOpportunityFactory().ListOpportunityIds_ByContactsId(contactsid);// 关联关系创建时间倒序排列
			List<Opportunity> os = business.opportunityFactory().ListByUUIDList(idList);
			//List<Wo> wos = Wo.copier.copy(os);
			List<WrapOutOpportunity> WrapOutOpportunityList = Wo.copier_to_WrapOut.copy(os);
			WrapOutOpportunityList = opportunityService.supplementWrapOutOpportunityList(business, WrapOutOpportunityList);
			List<Wo> wos = Wo.copier_to_Wo.copy(WrapOutOpportunityList);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends WrapOutOpportunity {
		/**
		 *
		 */
		private static final long serialVersionUID = -517675598771387988L;
		//static WrapCopier<Opportunity, Wo> copier = WrapCopierFactory.wo(Opportunity.class, Wo.class, null, JpaObject.FieldsInvisible);
		static WrapCopier<Opportunity, WrapOutOpportunity> copier_to_WrapOut = WrapCopierFactory.wo(Opportunity.class, WrapOutOpportunity.class, null,
				JpaObject.FieldsInvisible);
		static WrapCopier<WrapOutOpportunity, Wo> copier_to_Wo = WrapCopierFactory.wo(WrapOutOpportunity.class, Wo.class, null, JpaObject.FieldsInvisible);

	}
}
