package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.jaxrs.common.StringWCRMUtils;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;
import com.x.wcrm.core.entity.Customer;

/**
 * 根据创建时间，某一时间段内，我和我下属负责的客户列表
 */
public class ActionListPagingLikeNextTimeRang extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListPagingLikeNextTimeRang.class);

	// 所有当前用户所有递归下级的客户，按照时间倒序排列。
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement)
			throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);

			if (null == wi.getBegintime() || null == wi.getEndtime()) {
				throw new Exception("开始时间，结束时间不能为null");
			}
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Customer> os = customerStatisticPermissionService.getNextTimeList_MyDuty(ThisApplication.context(), business, effectivePerson,
					adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType(), sdf.parse(wi.getBegintime()), sdf.parse(wi.getEndtime()));
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			long count = customerStatisticPermissionService.getNextTimeCount_MyDuty(ThisApplication.context(), business, effectivePerson, wi.getKey(),
					sdf.parse(wi.getBegintime()), sdf.parse(wi.getEndtime()));
			result.setCount(count);

			return result;
		}
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 5220686039082993620L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

	public static class Wi extends ListStatisticPagingWi {

	}
}
