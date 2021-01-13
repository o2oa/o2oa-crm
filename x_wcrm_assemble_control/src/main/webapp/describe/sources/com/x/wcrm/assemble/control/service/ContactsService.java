package com.x.wcrm.assemble.control.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutContacts;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;

public class ContactsService {

	private static String READ_ONLY = "readonly";
	private static String READ_WRITE = "readandwrite";

	public Contacts initDefaultValue(EffectivePerson effectivePerson, Contacts contacts) {
		if (null == contacts.getDecision() || StringUtils.isBlank(contacts.getDecision())) {
			/* 是否为关键决策人0 不是，1是 */
			contacts.setDecision("0");
		}

		if (null == contacts.getSex() || StringUtils.isBlank(contacts.getSex())) {
			/* 默认性别男 */
			contacts.setSex("男");
		}
		//补全创建者为当前登录人
		if (null == contacts.getCreateuser() || StringUtils.isEmpty(contacts.getCreateuser()) || StringUtils.isBlank(contacts.getCreateuser())) {
			contacts.setCreateuser(effectivePerson.getDistinguishedName());
		}

		//补全负责人为当前登录人
		if (null == contacts.getOwneruser() || StringUtils.isEmpty(contacts.getOwneruser()) || StringUtils.isBlank(contacts.getOwneruser())) {
			contacts.setOwneruser(effectivePerson.getDistinguishedName());
		}

		return contacts;
	}

	public Contacts createFromCustomer(EffectivePerson effectivePerson, Contacts contacts, Customer customer) {
		contacts.setCellphone(customer.getCellphone());
		contacts.setContactsname(customer.getCustomername());
		contacts.setCustomerid(customer.getId());
		contacts.setDetailaddress(customer.getAddress());
		contacts.setRemark(customer.getRemark());
		contacts.setTelephone(customer.getTelephone());
		return contacts;
	}

	//根据客户id列出联系人列表。
	public List<Contacts> ListByCustomerId(EntityManagerContainer emc, Business business, String id) throws Exception {
		List<Contacts> os = business.contactsFactory().ListByCustomerId(id);
		return os;
	}

	//联系人隶属的客户是否公海
	public boolean IsPool(Business business, String _contactsId) throws Exception {
		Contacts contacts = business.contactsFactory().get(_contactsId);
		boolean _IsPool = business.customerFactory().isPool(contacts.getCustomerid());
		return _IsPool;
	}

	//单纯移出
	public List<Contacts> remove_owner_only(EntityManagerContainer emc, EffectivePerson effectivePerson, List<Contacts> os, String _new_distinguishName)
			throws Exception {
		System.out.println("_new_distinguishName2="+_new_distinguishName);
		if (ListTools.isNotEmpty(os)) {
			for (Contacts o : os) {
				o.setOwneruser(_new_distinguishName);
				System.out.println("_new_distinguishName3="+_new_distinguishName);
				//emc.beginTransaction(Contacts.class);
				emc.check(o, CheckPersistType.all);
				//emc.commit();
			}
		}

		return os;
	}

	// 转为团队成员
	public List<Contacts> transfer_owner_to_teammembers(EntityManagerContainer emc, EffectivePerson effectivePerson, List<Contacts> os,
			String _new_distinguishName, String _read_or_write) throws Exception {
		//		List<Contacts> os = emc.list(Contacts.class, idList);
		if (ListTools.isNotEmpty(os)) {
			for (Contacts o : os) {
				String _original_OwnerUser = o.getOwneruser();
				List<String> _original_OwnerUser_List = new ArrayList<String>();
				_original_OwnerUser_List.add(_original_OwnerUser);

				o.setOwneruser(_new_distinguishName);

				// 转为团队成员,read权限
				if (StringUtils.equalsIgnoreCase(_read_or_write, READ_ONLY)) {
					List<String> readerList = o.getReaderUserIds();
					readerList = ListTools.add(readerList, true, true, _original_OwnerUser_List);
					o.setReaderUserIds(readerList);
				}

				// 转为团队成员,read && write 权限
				if (StringUtils.equalsIgnoreCase(_read_or_write, READ_WRITE)) {
					List<String> writeList = o.getReaderUserIds();
					writeList = ListTools.add(writeList, true, true, _original_OwnerUser_List);
					o.setWriterUserIds(writeList);
				}
				//emc.beginTransaction(Contacts.class);
				emc.check(o, CheckPersistType.all);
				//emc.commit();

			}
		}

		return os;
	}

	/*
	 * 对联系人对象列表，补充其关联的 客户对象。
	 * */
	public List<WrapOutContacts> supplementWrapOutContactsList(Business business, List<WrapOutContacts> os) throws Exception {

		String _customerid = ""; //联系人对象中存储的客户id
		Customer _customer = null; //客户对象

		for (WrapOutContacts wo : os) {
			//客户
			_customerid = wo.getCustomerid();
			if (StringUtils.isNotEmpty(_customerid) && StringUtils.isNotBlank(_customerid)) {
				_customer = business.customerFactory().get(_customerid);
				if (_customer != null) {
					wo.setCustomer(_customer);
				}
			}
		}

		return os;
	}

	/*
	 * 对联系人对象(单个对象)，补充其关联的 客户对象。
	 * */
	public WrapOutContacts supplementWrapOutContacts(Business business, WrapOutContacts wo) throws Exception {

		String _customerid = ""; //联系人对象中存储的客户id
		Customer _customer = null; //客户对象

		//客户
		_customerid = wo.getCustomerid();
		if (StringUtils.isNotEmpty(_customerid) && StringUtils.isNotBlank(_customerid)) {
			_customer = business.customerFactory().get(_customerid);
			if (_customer != null) {
				wo.setCustomer(_customer);
				return wo;
			} else {
				return wo;
			}
		} else {

			return wo;
		}

	}

}
