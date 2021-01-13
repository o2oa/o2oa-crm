package com.x.wcrm.assemble.control.jaxrs.leads;

import com.x.base.core.project.jaxrs.WoId;
import com.x.wcrm.assemble.control.service.LeadsPermissionService;
import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutLeads;
import com.x.wcrm.core.entity.Leads;

//删除线索（owner，管理员，crm管理员可以删除）
public class ActionDelete extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			Leads o = emc.find(id, Leads.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Leads.class);
			}
			LeadsPermissionService leadsPermissionService = new LeadsPermissionService();
			/**
			 * 权限判断 1.如果线索的负责人为空，那么只有管理员可以删除 2.如果线索的负责人不为空，负责人，管理员，CRM管理员角色 可以删除
			 */
			String _owneruser = o.getOwneruser();
			if (StringUtils.isEmpty(_owneruser) || StringUtils.isBlank(_owneruser)) {
				//1
				if (!business.isManager(effectivePerson)) {
					//不是管理员
					throw new ExceptionLeadsNoPermission(o.getId());
				}
			} else {
				//2
				if (!business.isManager(effectivePerson)) {
					if (!StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
						//不是负责人
						throw new ExceptionLeadsNoPermission(o.getId());
					}
					/*//不是管理员
					throw new ExceptionLeadsNoPermission(o.getId());*/
				}
			}
			/*
			 * 权限判断通过
			 */

			Wo wo = new Wo();
			leadsPermissionService.delete(id,effectivePerson);

			result.setData(wo);
			return result;
		}
	}

	static class Wo extends WoId {

	}
}
