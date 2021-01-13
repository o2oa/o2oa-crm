package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.AbstractContext;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;
import com.x.base.core.project.x_organization_assemble_express;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.service.PermissionServiceBase;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;

public class Action_Customer_CountByProvince extends BaseAction {

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//参数
			String _key = wi.getKey();
			Date _begintime = sdf.parse(wi.getBegintime());
			Date _endtime = sdf.parse(wi.getEndtime());
			List<String> _persons = new ArrayList<String>();
			/*List<String> _me_collection = new ArrayList<String>();
			_me_collection.add(effectivePerson.getDistinguishedName());*/
			if (ListTools.isEmpty(wi.getUnitList()) && ListTools.isEmpty(wi.getPersonNameList())) {
				/*_persons.add(effectivePerson.getDistinguishedName());
				List<String> _subNestedPersons = getListWithPersonSubNested(ThisApplication.context(), effectivePerson, _me_collection); // 所有下属
				if (ListTools.isNotEmpty(_subNestedPersons)) {
					_persons = ListTools.add(_persons, true, true, _subNestedPersons);
				}*/
				_persons = permissionServiceBase.getPersonList_MyAndSubNested(ThisApplication.context(), effectivePerson);
			}else{
				// 人员列表，组织列表有一项或者多项不为空。根据传入的组织和人员进行计算
				List<String> _persons_p = this.convertToPerson(business, wi.getPersonNameList());
				List<String> _persons_u = this.convertToPerson(business, wi.getUnitList());
				_persons = ListTools.add(_persons_p, true, true, _persons_u);
			}

			List<SimpleKV> os = business.customerFactory().count_by_province(_persons,_begintime, _endtime);
			List<Wo> wos = new ArrayList<>();
			for (SimpleKV simpleKV : os) {
				Wo o = new Wo();
				o.setProvinceName(simpleKV.getKey());
				o.setCount(simpleKV.getValue());
				wos.add(o);
			}
			result.setData(wos);
			return result;
		}

	}

	static class Wo extends GsonPropertyObject {
		String provinceName;
		String count;

		public String getProvinceName() {
			return provinceName;
		}

		public void setProvinceName(String provinceName) {
			this.provinceName = provinceName;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

	}

	public static class Wi extends ListStatisticPagingWi {

	}

	/*public static class WiP extends GsonPropertyObject {

		@FieldDescribe("个人")
		private List<String> personList = new ArrayList<>();

		public List<String> getPersonList() {
			return personList;
		}

		public void setPersonList(List<String> personList) {
			this.personList = personList;
		}

	}
	static class WoPersonListAbstract extends GsonPropertyObject {

		@FieldDescribe("个人识别名")
		private List<String> personList = new ArrayList<>();

		public List<String> getPersonList() {
			return personList;
		}

		public void setPersonList(List<String> personList) {
			this.personList = personList;
		}

	}

	protected static Class<?> applicationOrganizationClass = x_organization_assemble_express.class;
	//递归获得当前用户的所有下级人员，根据distinguishName计算
	public List<String> getListWithPersonSubNested(AbstractContext context, EffectivePerson effectivePerson, Collection<String> collection) throws Exception {
		WiP wip = new WiP();
		if (null != collection) {
			wip.getPersonList().addAll(collection);
		}

		WoPersonListAbstract wop = context.applications().postQuery(applicationOrganizationClass, "person/list/person/sub/nested", wip).getData(WoPersonListAbstract.class);
		return wop.getPersonList();
	}*/
}
