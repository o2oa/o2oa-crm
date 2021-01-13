package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;

//全部客户按照行业
public class Action_Customer_CountByIndustry extends BaseAction {
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<String> _persons = new ArrayList<String>();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ListTools.isEmpty(wi.getUnitList()) && ListTools.isEmpty(wi.getPersonNameList())) {
				_persons = permissionServiceBase.getPersonList_MyAndSubNested(ThisApplication.context(), effectivePerson);
			}else{
				// 人员列表，组织列表有一项或者多项不为空。根据传入的组织和人员进行计算
				List<String> _persons_p = this.convertToPerson(business, wi.getPersonNameList());
				List<String> _persons_u = this.convertToPerson(business, wi.getUnitList());
				_persons = ListTools.add(_persons_p, true, true, _persons_u);
			}

			List<SimpleKV> os = business.customerStatisticFactory().count_by_industry(_persons, null,
					sdf.parse(wi.getBegintime()), sdf.parse(wi.getEndtime()));
			List<Wo> wos = new ArrayList<>();
			for (SimpleKV simpleKV : os) {
				Wo o = new Wo();
				o.setIndustry(simpleKV.getKey());
				o.setCount(simpleKV.getValue());
				wos.add(o);
			}
			result.setData(wos);
			return result;
		}

	}

	public static class Wi extends ListStatisticPagingWi {

	}

	static class Wo extends GsonPropertyObject {
		private String industry;
		String count;

		public String getIndustry() {
			return industry;
		}

		public void setIndustry(String industry) {
			this.industry = industry;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

	}
}
