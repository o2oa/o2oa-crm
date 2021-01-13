package com.x.wcrm.assemble.control.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.x.base.core.project.AbstractContext;
import com.x.base.core.project.x_organization_assemble_express;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;

public class PermissionServiceBase {


	protected static Class<?> applicationOrganizationClass = x_organization_assemble_express.class;

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

	//递归获得当前用户的所有下级人员，根据distinguishName计算
	public List<String> getListWithPersonSubNested(AbstractContext context, EffectivePerson effectivePerson, Collection<String> collection) throws Exception {
		Wi wi = new Wi();
		if (null != collection) {
			wi.getPersonList().addAll(collection);
		}
		Wo wo = context.applications().postQuery(applicationOrganizationClass, "person/list/person/sub/nested", wi).getData(Wo.class);
		return wo.getPersonList();
	}

	public static class Wi extends GsonPropertyObject {

		@FieldDescribe("个人")
		private List<String> personList = new ArrayList<>();

		public List<String> getPersonList() {
			return personList;
		}

		public void setPersonList(List<String> personList) {
			this.personList = personList;
		}

	}

	public static class Wo extends WoPersonListAbstract {
	}
	
	
	/**
	 * 获得一个List<String>的distinguishName，包括我和我的递归下属
	 * */
	public List<String> getPersonList_MyAndSubNested(AbstractContext context, EffectivePerson effectivePerson)
			throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		return _persons;
	}
}
