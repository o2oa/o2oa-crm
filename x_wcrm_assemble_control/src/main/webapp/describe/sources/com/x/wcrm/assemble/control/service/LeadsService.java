package com.x.wcrm.assemble.control.service;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.jaxrs.leads.BaseAction;
import com.x.wcrm.core.entity.Leads;

public class LeadsService {

	// 初始化信息
	public Leads initDefaultValue(EffectivePerson effectivePerson, Leads leads) {
		// 线索的是否转化标志。如果为空默认未转化
		if (StringUtils.isBlank(leads.getIstransform())) {
			// 0 未转化
			leads.setIstransform(BaseAction.NOT_TRANSFORM);
		}

		if (StringUtils.isBlank(leads.getCreateuser())) {
			leads.setCreateuser(effectivePerson.getDistinguishedName());
		}

		if (StringUtils.isBlank(leads.getOwneruser())) {
			leads.setOwneruser(effectivePerson.getDistinguishedName());
		}
		return leads;
	}

	//填充原数据的非空项。
	public Leads keepOriginalData(EffectivePerson effectivePerson, Leads original_leads, Leads wi_leads) {

		return original_leads;
	}

}
