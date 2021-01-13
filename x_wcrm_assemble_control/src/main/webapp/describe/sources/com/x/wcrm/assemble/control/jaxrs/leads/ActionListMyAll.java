package com.x.wcrm.assemble.control.jaxrs.leads;

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
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.core.entity.Leads;

class ActionListMyAll extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListMyAll.class);

	// 所有当前用户所有递归下级的线索，按照时间倒序排列。
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Leads> os = leadsPermissionService.getList_MyAll(ThisApplication.context(), business, effectivePerson);
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Leads {
		/**
		 *
		 */
		private static final long serialVersionUID = 930267889656181127L;
		static WrapCopier<Leads, Wo> copier = WrapCopierFactory.wo(Leads.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

}
