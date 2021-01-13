package com.x.wcrm.assemble.control.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.x.base.core.project.AbstractContext;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;

public class ContactsPermissionService extends PermissionServiceBase {

	private static Logger logger = LoggerFactory.getLogger(ContactsPermissionService.class);

	//获得所有下属的联系人
	public List<Contacts> getList_PersonSubNested(AbstractContext context, Business business, EffectivePerson effectivePerson, Integer adjustPage,
												  Integer adjustPageSize, String keyString, String orderFieldName, String orderType) throws Exception {
		logger.info("getList_PersonSubNested run!");
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属

		List<Contacts> os = business.contactsFactory().ListByOwnerList(_persons, adjustPage, adjustPageSize, keyString, orderFieldName, orderType);
		return os;
	}

	//获得自己负责的联系人
	public List<Contacts> getList_MyDuty(AbstractContext context, Business business, EffectivePerson effectivePerson, Integer adjustPage,
										 Integer adjustPageSize, String keyString, String orderFieldName, String orderType)
			throws Exception {
		List<String> _me = new ArrayList<String>();
		_me.add(effectivePerson.getDistinguishedName());
		List<Contacts> os = business.contactsFactory().ListByOwnerList(_me, adjustPage, adjustPageSize, keyString, orderFieldName, orderType);
		return os;
	}

	//获取我参与的两部分数据：1，我是负责人（owner是当前处理人），2我是团队成员（reader，writer 中包含当前处理人）
	public List<Contacts> getList_MyParticipate(AbstractContext context, Business business,
			EffectivePerson effectivePerson) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<Contacts> os = business.contactsFactory().List_OwnerEqual_Or_ReadersMember_Or_WritesMember(_me);
		return os;
	}

	/*//获得自己负责的,和我的下属负责联系人
	public List<Contacts> getList_MyDuty_And_SubNestedDuty(AbstractContext context, Business business,
			EffectivePerson effectivePerson) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		List<Contacts> os = business.contactsFactory().ListByOwnerList(_persons);
		return os;
	}
*/
	//1我自己负责，我下属负责的。2我自己作为团队成员（包括读写权限，和制度权限）的联系人
	public List<Contacts> getList_MyAll(AbstractContext context, Business business, EffectivePerson effectivePerson)
			throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		List<Contacts> os = business.contactsFactory().ListByOwnerList_And_TeamMembersReadAndWrite(_me_collection, _me);
		return os;
	}

	//获得自己负责的客户，数量
	public long getList_MyDuty_Count(AbstractContext context, Business business, EffectivePerson effectivePerson, String keyString) throws Exception {
		List<String> _me = new ArrayList<String>();
		_me.add(effectivePerson.getDistinguishedName());
		long count = business.contactsFactory().ListByOwnerList_Count(_me, keyString);
		return count;
	}

	//获得自己负责的,和我的下属负责客户
	public List<Contacts> getList_MyDuty_And_SubNestedDuty(AbstractContext context, Business business, EffectivePerson effectivePerson, Integer adjustPage,
														   Integer adjustPageSize, String keyString, String orderFieldName, String orderType) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		//List<Customer> os = business.customerFactory().ListByOwnerList(_persons);
		List<Contacts> os = business.contactsFactory().ListByOwnerList(_persons, adjustPage, adjustPageSize, keyString, orderFieldName, orderType);
		return os;
	}

	//获得自己负责的,和我的下属负责客户,数量
	public long getList_MyDuty_And_SubNestedDuty_Count(AbstractContext context, Business business, EffectivePerson effectivePerson, String keyString)
			throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		//List<Customer> os = business.customerFactory().ListByOwnerList(_persons);
		long count = business.contactsFactory().ListByOwnerList_Count(_persons, keyString);
		return count;
	}

	//获得我的下属负责客户,数量
	public long getList_SubNestedDuty_Count(AbstractContext context, Business business, EffectivePerson effectivePerson, String keyString)
			throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		//_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		//List<Customer> os = business.customerFactory().ListByOwnerList(_persons);
		long count = business.contactsFactory().ListByOwnerList_Count(_persons, keyString);
		return count;
	}

	//获得自己负责的,和我的下属负责联系人 数量
	public long getList_MyDuty_And_SubNestedDuty_Count(AbstractContext context, Business business,
													   EffectivePerson effectivePerson, String keyString, Date begintime, Date endtime) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); //所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		long count = business.contactsStatisticFactory().ListByOwnerList_Count(_persons, keyString, begintime, endtime);
		return count;
	}

}
