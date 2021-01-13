package com.x.wcrm.assemble.control.jaxrs.leads;

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
import com.x.wcrm.core.entity.Leads;

class ActionListMyDutyAndSubNestedDuty extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListMyDutyAndSubNestedDuty.class);

	// 所有当前用户所有递归下级的线索，按照时间倒序排列。
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize,
			JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// List<Leads> os =
			// leadsPermissionService.getList_MyDuty_And_SubNestedDuty(ThisApplication.context(),
			// business, effectivePerson);
			List<Leads> os = leadsPermissionService.getList_MyDuty_And_SubNestedDuty(ThisApplication.context(),
					business, effectivePerson, adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(),
					wi.getOrderType(), null, null);
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			long count = leadsPermissionService.getList_MyDuty_And_SubNestedDuty_Count(ThisApplication.context(),
					business, effectivePerson, wi.getKey(), null, null);
			result.setCount(count);
			return result;
		}
	}

	public static class Wo extends Leads {
		private static final long serialVersionUID = 5220686039082993620L;
		static WrapCopier<Leads, Wo> copier = WrapCopierFactory.wo(Leads.class, Wo.class, null,
				JpaObject.FieldsInvisible, false);
	}

	public static class Wi extends ListPagingWi {

	}
}
