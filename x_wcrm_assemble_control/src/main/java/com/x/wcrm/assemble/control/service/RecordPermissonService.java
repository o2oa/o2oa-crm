package com.x.wcrm.assemble.control.service;

import java.util.Date;
import java.util.List;

import com.x.base.core.project.AbstractContext;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Record;

public class RecordPermissonService extends PermissionServiceBase {

	public List<Record> getList_MyDuty_And_SubNestedDuty(AbstractContext context, Business business,
			EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
//		String _me = effectivePerson.getDistinguishedName();
//		List<String> _me_collection = new ArrayList<String>();
//		_me_collection.add(effectivePerson.getDistinguishedName());
//
//		List<String> _persons = new ArrayList<String>();
//		_persons.add(_me);
//		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
//		if (ListTools.isNotEmpty(_subNestedPersons)) {
//			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
//		}
		
		List<String> _persons = this.getPersonList_MyAndSubNested(context, effectivePerson);
		List<Record> os = business.recordFactory().List_ByCreatorNameList(_persons, adjustPage, adjustPageSize,
				keyString, orderFieldName, orderType, begintime, endtime);
		return os;
	}

	// 获得自己负责的,和我的下属负责客户,数量
	public long get_My_And_SubNested_Count(AbstractContext context, Business business, EffectivePerson effectivePerson,
			String keyString, Date begintime, Date endtime) throws Exception {
//		String _me = effectivePerson.getDistinguishedName();
//		List<String> _me_collection = new ArrayList<String>();
//		_me_collection.add(effectivePerson.getDistinguishedName());
//
//		List<String> _persons = new ArrayList<String>();
//		_persons.add(_me);
//		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
//		if (ListTools.isNotEmpty(_subNestedPersons)) {
//			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
//		}
		List<String> _persons = this.getPersonList_MyAndSubNested(context, effectivePerson);
		long count = business.recordFactory().Count_ByCreatorNameList(_persons, keyString, begintime, endtime);
		return count;
	}


}
