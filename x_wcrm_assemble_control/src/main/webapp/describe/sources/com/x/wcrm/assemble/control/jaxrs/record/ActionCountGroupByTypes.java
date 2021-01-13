package com.x.wcrm.assemble.control.jaxrs.record;

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
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleValues;
import com.x.wcrm.assemble.control.service.RecordPermissonService;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;

public class ActionCountGroupByTypes extends BaseAction {

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			RecordPermissonService recordPermissonService = new RecordPermissonService();
			//获取本人和本人的递归下属
			List<String> _person = recordPermissonService.getPersonList_MyAndSubNested(ThisApplication.context(),
					effectivePerson);
			//获取模块ID和对应的跟进记录数量
			List<SimpleKV> os = business.recordFactory().Count_GroupByTypes(_person, null, sdf.parse(wi.getBegintime()),
					sdf.parse(wi.getEndtime()));
			List<Wo> wos = new ArrayList<>();
			for (SimpleKV e : os) {
				Wo o = new Wo();

				o.setTypes(e.getKey());
				o.setCount(e.getCount());
				//从枚举中根据模块ID获取对应的模块名称
				String module = WCRMModuleValues.getValueIgnoreCase(e.getKey());
				o.setTypesname(module);
				wos.add(o);
			}

			result.setData(wos);
			return result;
		}
	}

	static class Wi extends ListStatisticPagingWi {

	}

	static class Wo extends GsonPropertyObject {
		String types;
		String typesname;
		long count;

		public String getTypes() {
			return types;
		}

		public void setTypes(String types) {
			this.types = types;
		}

		public String getTypesname() {
			return typesname;
		}

		public void setTypesname(String typesname) {
			this.typesname = typesname;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

	}
}
