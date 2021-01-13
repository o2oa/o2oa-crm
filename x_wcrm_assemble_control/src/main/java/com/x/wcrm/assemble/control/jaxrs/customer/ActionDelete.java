package com.x.wcrm.assemble.control.jaxrs.customer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.wrapout.WrapOutCustomer;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Opportunity;

//删除客户（owner，管理员，crm管理员可以删除）
public class ActionDelete extends BaseAction {
	private static final String SUC_STRING = "成功删除客户.";

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			Customer o = emc.find(id, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Customer.class);
			}

			/**
			 * 权限判断 1.如果客户的负责人为空，那么只有管理员可以删除（客户公海场景） 2.如果客户的负责人不为空，负责人，管理员，CRM管理员角色 可以删除
			 */
			String _owneruser = o.getOwneruser();
			if (StringUtils.isEmpty(_owneruser) || StringUtils.isBlank(_owneruser)) {
				//1
				if (!business.isManager(effectivePerson)) {
					//不是管理员
					throw new ExceptionCustomerNoPermission(o.getId());
				}
			} else {
				//2
				if (!business.isManager(effectivePerson)) {
					if (!StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
						//不是负责人
						throw new ExceptionCustomerNoPermission(o.getId());
					}
					/*//不是管理员
					throw new ExceptionCustomerNoPermission(o.getId());*/
				}
			}
			/*权限判断通过*/

			/*
			 * 关联判断：有商机、联系人、合同、则不能删除 
			 * */
			String _id = o.getId();
			boolean isPass = true;
			String noticeString = "";
			List<Opportunity> _opportunityList = opportunityService.ListByCustomerId(emc, business, _id);
			if (ListTools.isNotEmpty(_opportunityList)) {
				//有商机关联
				isPass = false;
				noticeString += "关联了商机：" + ListTools.toStringJoin(_opportunityList, "#");
			}

			List<Contacts> _contactsList = contactsService.ListByCustomerId(emc, business, _id);
			if (ListTools.isNotEmpty(_contactsList)) {
				//有联系人关联
				isPass = false;
				noticeString += "关联了联系人：" + ListTools.toStringJoin(_contactsList, "#");
			}

			/*
			 * 有合同关联（未实现）
			 * */
			Wo wo = Wo.copier.copy(o);
			if (isPass) {
				wo.setIsdeletesuccess(true);
				wo.setNotice(noticeString);
				emc.beginTransaction(Customer.class);
				emc.remove(o);
				emc.commit();
			} else {
				wo.setIsdeletesuccess(false);
				wo.setNotice(SUC_STRING);
			}

			result.setData(wo);
			return result;
		}
	}

	public static class Wo extends WrapOutCustomer {
		private static final long serialVersionUID = 3589487311528550620L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible);

		private boolean isdeletesuccess;

		private String notice;

		public boolean isIsdeletesuccess() {
			return isdeletesuccess;
		}

		public void setIsdeletesuccess(boolean isdeletesuccess) {
			this.isdeletesuccess = isdeletesuccess;
		}

		public String getNotice() {
			return notice;
		}

		public void setNotice(String notice) {
			this.notice = notice;
		}
	}
}
