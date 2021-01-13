package com.x.wcrm.assemble.control.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleValues;
import com.x.wcrm.assemble.control.jaxrs.record.BaseAction;
import com.x.wcrm.assemble.control.staticconfig.FollowConfig;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Leads;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.Record;

public class RecordService {
	// 初始化信息
	public Record initDefaultValue(EffectivePerson effectivePerson, Record record) {
		// 补全默认创建人
		if (null == record.getCreateuser() || StringUtils.isBlank(record.getCreateuser())) {
			record.setCreateuser(effectivePerson.getDistinguishedName());
		}

		return record;
	}

	/**
	 * 
	 * @param types 为：leads,customer,opportunity,contacts
	 */
	public void updateNextTimeCRM(String types, String typeId, Date nexttime) throws Exception {

		//客户
		if (StringUtils.equals(types, WCRMModuleValues.CUSTOMER.getWcrmId())) {
			try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
				Customer o = emc.find(typeId, Customer.class);
				if (null != o) {
					o.setNexttime(nexttime);
					o.setFollow(FollowConfig.FOLLOWED);
				}
				emc.beginTransaction(Customer.class);
				emc.persist(o, CheckPersistType.all);
				emc.commit();
			}
		}

		//线索
		if (StringUtils.equals(types, WCRMModuleValues.LEADS.getWcrmId())) {
			try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
				Leads o = emc.find(typeId, Leads.class);
				if (null != o) {
					o.setNexttime(nexttime);
					o.setFollow(FollowConfig.FOLLOWED);
				}
				emc.beginTransaction(Customer.class);
				emc.persist(o, CheckPersistType.all);
				emc.commit();
			}
		}

		//联系人
		if (StringUtils.equals(types, WCRMModuleValues.CONTACTS.getWcrmId())) {
			try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
				Contacts o = emc.find(typeId, Contacts.class);
				if (null != o) {
					o.setNexttime(nexttime);
					o.setFollow(FollowConfig.FOLLOWED);
				}
				emc.beginTransaction(Customer.class);
				emc.persist(o, CheckPersistType.all);
				emc.commit();
			}
		}

		//商机
		if (StringUtils.equals(types, WCRMModuleValues.OPPORTUNITY.getWcrmId())) {
			try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
				Opportunity o = emc.find(typeId, Opportunity.class);
				if (null != o) {
					o.setNexttime(nexttime);
					o.setFollow(FollowConfig.FOLLOWED);
				}
				emc.beginTransaction(Customer.class);
				emc.persist(o, CheckPersistType.all);
				emc.commit();
			}
		}

	}
}
