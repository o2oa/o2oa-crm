package com.x.wcrm.assemble.control.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Leads;

public class CustomerService extends SimpleService {
	public static final String LOCK = "1"; //锁定
	public static final String UNLOCK = "0"; //未锁定

	public static final String MAKE_A_DEAL = "已成交";
	public static final String UN_MAKE_A_DEAL = "未成交";

	public Customer initDefaulVal(Customer customer, EffectivePerson effectivePerson) {

		if (null == customer.getIslock() || StringUtils.isBlank(customer.getIslock()) || StringUtils.isEmpty(customer.getIslock())) {
			// 0未锁定，1锁定
			customer.setIslock(UNLOCK);
		}
		if (null == customer.getDealstatus() || StringUtils.isBlank(customer.getDealstatus())) {
			// 成交，未成交
			customer.setDealstatus(UN_MAKE_A_DEAL);
		}

		if (null == customer.getCreateuser() || StringUtils.isBlank(customer.getCreateuser())) {
			// 设置创建者
			String _creator = effectivePerson.getDistinguishedName();
			customer.setCreateuser(_creator);

			List<String> writerList = customer.getWriterUserIds();

			if (null != writerList && writerList.indexOf(_creator) < 0) {
				writerList.add(_creator);
			}
			customer.setWriterUserIds(writerList);
		}

		if (null == customer.getOwneruser() || StringUtils.isBlank(customer.getOwneruser())) {
			// 设置责任人
			String _owner = effectivePerson.getDistinguishedName();
			customer.setOwneruser(effectivePerson.getDistinguishedName());

			List<String> writerList = customer.getWriterUserIds();
			if (null != writerList && writerList.indexOf(_owner) < 0) {
				writerList.add(_owner);
			}
			customer.setWriterUserIds(writerList);
		}

		return customer;
	}

	// 线索转客户
	public Customer transformToCustomer(Leads leads, Customer customer) {
		customer.setCustomername(leads.getName());
		customer.setAddress(leads.getAddress());
		customer.setCellphone(leads.getCellphone());
		customer.setDetailaddress(leads.getAddress());
		customer.setIndustry(leads.getIndustry());
		customer.setLevel(leads.getLevel());
		customer.setRemark(leads.getRemark());
		customer.setNexttime(leads.getNexttime());
		customer.setTelephone(leads.getTelephone());
		customer.setSource(leads.getSource());
		return customer;
	}

	// 给客户对象增加读者，作者人员
	public Customer AddRelevantPerson(Customer o, List<String> personList, String permissionSymbol) throws Exception {
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
	
	//我负责的客户（责任人是当前用户（owner））
	
	//我下属的客户（先计算出我的所有下属（根据o2汇报关系计算，这里可能有点问题，crm中一个人可以有多个汇报对象。））

	//所有客户（我负责的客户，和我下属的客户的合集）
	
	
	//获得所有符合公海条件的客户ids
	public List<String> getIdList_matchPoolRule(){
		return null;
	}
	
}
