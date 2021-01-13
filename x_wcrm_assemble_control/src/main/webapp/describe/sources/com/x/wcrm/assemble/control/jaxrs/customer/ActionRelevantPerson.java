package com.x.wcrm.assemble.control.jaxrs.customer;

import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleId;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Opportunity;

//为客户对象增加 读者，作者
public class ActionRelevantPerson extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(ActionRelevantPerson.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement, String w_r_symbol) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			// business 先留着，后面关联数据继续使用。
			//
			Customer o = emc.find(id, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Customer.class);
			}
			logger.info(wi.toString());
			o = customerService.AddRelevantPerson(o, wi.getPersonList(), w_r_symbol);
			// o = customerService.AddRelevantPerson(o, null, w_r_symbol);
			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);

			List<String> _relationTypeList = wi.getRelationTypeList();
			//判断是否同时关联商机
			boolean opportunityMatch = _relationTypeList.stream().anyMatch(item -> item.equalsIgnoreCase(WCRMModuleId.opportunity.toString()));

			//同时添加商机成员
			if (opportunityMatch) {
				List<Opportunity> _opportunity_list = opportunityService.ListByCustomerId(emc, business, id);
				if (ListTools.isNotEmpty(_opportunity_list)) {
					for (Opportunity opportunity : _opportunity_list) {
						Opportunity _newInst = opportunityService.AddRelevantPerson(emc, business, opportunity, wi.getPersonList(), w_r_symbol);
						emc.beginTransaction(Opportunity.class);
						emc.persist(_newInst, CheckPersistType.all);
						emc.commit();
					}
				}
			} else {
				logger.info("not opportunityMatch！！！！");
			}

			//预留合同

			emc.commit();

			logger.info(o.toString());
			Wo wo = new Wo();
			wo = Wo.copier.copy(o);
			result.setData(wo);
			return result;
		}
	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("人员列表，distinguishname")
		List<String> personList;

		@FieldDescribe("listModuleId中的，opportunity或者contacts或者xx等")
		private List<String> relationTypeList;

		public List<String> getPersonList() {
			return personList;
		}

		public void setPersonList(List<String> personList) {
			this.personList = personList;
		}

		public List<String> getRelationTypeList() {
			return relationTypeList;
		}

		public void setRelationTypeList(List<String> relationTypeList) {
			this.relationTypeList = relationTypeList;
		}

	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = -7207872030774397470L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

}
