package com.x.wcrm.assemble.control.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutOpportunity;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityType;

public class OpportunityService extends SimpleService {

	private static String READ_ONLY = "readonly";
	private static String READ_WRITE = "readandwrite";

	//设置默认值
	public Opportunity initDefaulVal(Opportunity o, EffectivePerson effectivePerson) {

		if (null == o.getCreateuser() || StringUtils.isBlank(o.getCreateuser())) {
			// 设置创建者
			String _creator = effectivePerson.getDistinguishedName();
			o.setCreateuser(_creator);
		}

		if (null == o.getOwneruser() || StringUtils.isBlank(o.getOwneruser())) {
			// 设置责任人
			//			String _owner = effectivePerson.getDistinguishedName();
			o.setOwneruser(effectivePerson.getDistinguishedName());
		}

		return o;
	}

	//根据客户uuid列出对象列表。
	public List<Opportunity> ListByCustomerId(EntityManagerContainer emc, Business business, String id) throws Exception {
		List<Opportunity> os = business.opportunityFactory().ListByCustomerId(id);
		return os;
	}

	//单纯移出负责人
	public List<Opportunity> remove_owner_only(EntityManagerContainer emc, EffectivePerson effectivePerson, List<Opportunity> os, String _new_distinguishName)
			throws Exception {
		if (ListTools.isNotEmpty(os)) {
			for (Opportunity o : os) {
				o.setOwneruser(_new_distinguishName);
				//emc.beginTransaction(Opportunity.class);
				emc.check(o, CheckPersistType.all);
				//emc.commit();
			}
		}

		return os;
	}

	// 负责人转为团队成员
	public List<Opportunity> transfer_owner_to_teammembers(EntityManagerContainer emc, EffectivePerson effectivePerson, List<Opportunity> os,
			String _new_distinguishName, String _read_or_write) throws Exception {
		if (ListTools.isNotEmpty(os)) {
			for (Opportunity o : os) {
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

				//emc.beginTransaction(Opportunity.class);
				emc.check(o, CheckPersistType.all);
				//emc.commit();
			}
		}

		return os;
	}

	/*
	 * 对商机对象列表，补充其关联的 客户对象，商机状态组对象，商机状态对象。
	 * */
	public List<WrapOutOpportunity> supplementWrapOutOpportunityList(Business business, List<WrapOutOpportunity> os) throws Exception {

		String _customerid = ""; //商机对象中存储的客户id
		Customer _customer = null; //客户对象

		String _typeid = ""; //商机对象中存储的商机状态组id
		OpportunityType _opportunityType = null; //商机状态组对象。

		String _statusid = ""; //商机对象中存储的商机状态（销售阶段）id
		OpportunityStatus _opportunityStatus = null; //商机状态对象。

		for (WrapOutOpportunity wo : os) {

			//客户
			_customerid = wo.getCustomerid();
			_customer = business.customerFactory().get(_customerid);
			if (_customer != null) {
				wo.setCustomer(_customer);
			}

			//商机状态组
			_typeid = wo.getTypeid();
			_opportunityType = business.opportunityTypeFactory().get(_typeid);
			if (_opportunityType != null) {
				wo.setOpportunityType(_opportunityType);
			}

			//商机状态
			_statusid = wo.getStatusid();
			_opportunityStatus = business.opportunityStatusFactory().get(_statusid);
			if (_opportunityStatus != null) {
				wo.setOpportunityStatus(_opportunityStatus);
			}
		}

		return os;
	}

	/*
	 * 对商机对象(单个对象)，补充其关联的 客户对象，商机状态组对象，商机状态对象。
	 * */
	public WrapOutOpportunity supplementWrapOutOpportunity(Business business, WrapOutOpportunity wo) throws Exception {

		String _customerid = ""; //商机对象中存储的客户id
		Customer _customer = null; //客户对象

		String _typeid = ""; //商机对象中存储的商机状态组id
		OpportunityType _opportunityType = null; //商机状态组对象。

		String _statusid = ""; //商机对象中存储的商机状态（销售阶段）id
		OpportunityStatus _opportunityStatus = null; //商机状态对象。

		//客户
		_customerid = wo.getCustomerid();
		_customer = business.customerFactory().get(_customerid);
		if (_customer != null) {
			wo.setCustomer(_customer);
		}

		//商机状态组
		_typeid = wo.getTypeid();
		_opportunityType = business.opportunityTypeFactory().get(_typeid);
		if (_opportunityType != null) {
			wo.setOpportunityType(_opportunityType);
		}

		//商机状态
		_statusid = wo.getStatusid();
		_opportunityStatus = business.opportunityStatusFactory().get(_statusid);
		if (_opportunityStatus != null) {
			wo.setOpportunityStatus(_opportunityStatus);
		}

		return wo;
	}

	// 给商机对象增加读者，作者人员
	public Opportunity AddRelevantPerson(EntityManagerContainer emc, Business business, Opportunity o, List<String> personList, String permissionSymbol)
			throws Exception {
		//增加作者
		if (StringUtils.equalsAnyIgnoreCase(permissionSymbol, CustomerService.WRITE_SYMBOL)) {
			List<String> _writer_list = o.getWriterUserIds();
			_writer_list = ListTools.add(_writer_list, true, true, personList);
			o.setWriterUserIds(_writer_list);

			//整理读者列表
			List<String> readerList = o.getReaderUserIds();
			for (String _new_distinguishName : personList) {
				readerList.removeIf(s -> s.contentEquals(_new_distinguishName));
			}
			o.setReaderUserIds(readerList);
		}

		//增加读者
		if (StringUtils.equalsAnyIgnoreCase(permissionSymbol, CustomerService.READ_SYMBOL)) {
			List<String> _reader_list = o.getReaderUserIds();
			_reader_list = ListTools.add(_reader_list, true, true, personList);
			o.setReaderUserIds(_reader_list);

			//整理作者列表
			List<String> writerList = o.getWriterUserIds();
			for (String _new_distinguishName : personList) {
				writerList.removeIf(s -> s.contentEquals(_new_distinguishName));
			}
			o.setWriterUserIds(writerList);
		}
		return o;
	}

}
