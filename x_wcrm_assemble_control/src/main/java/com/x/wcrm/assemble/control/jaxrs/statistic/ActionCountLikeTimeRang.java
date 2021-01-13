package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.WrapOutCount;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleValues;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;

/**
 * 根据创建时间，某一时间段内，我和我下属负责的客户列表
 */
public class ActionCountLikeTimeRang extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionCountLikeTimeRang.class);

	// 所有当前用户所有递归下级的客户，按照时间倒序排列。
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);

			Business business = new Business(emc);
			long count;
			Wo wo_customer = new Wo();
			Wo wo_contacts = new Wo();
			//Wo wo_leads = new Wo();
			Wo wo_opportunity = new Wo();
			Wo wo_record = new Wo();

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//参数
			String _key = wi.getKey();
			Date _begintime = sdf.parse(wi.getBegintime());
			Date _endtime = sdf.parse(wi.getEndtime());

			/*System.out.println("_begintime="+_begintime);
			System.out.println("_endtime="+_endtime);*/

			List<Wo> wos = new ArrayList<Wo>();

			if (ListTools.isEmpty(wi.getUnitList()) && ListTools.isEmpty(wi.getPersonNameList())) {
				//客户
				count = customerStatisticPermissionService.getList_MyDuty_And_SubNestedDuty_Count(
						ThisApplication.context(), business, effectivePerson, _key, _begintime, _endtime);
				wo_customer.setCount(count);
				wo_customer.setModuleId(WCRMModuleValues.CUSTOMER.getWcrmId());
				wos.add(wo_customer);

				//联系人
				count = contactsPermissionService.getList_MyDuty_And_SubNestedDuty_Count(ThisApplication.context(),
						business, effectivePerson, _key, _begintime, _endtime);
				wo_contacts.setCount(count);
				wo_contacts.setModuleId(WCRMModuleValues.CONTACTS.getWcrmId());
				wos.add(wo_contacts);

				//				//线索
				//				count = leadsPermissionService.getList_MyDuty_And_SubNestedDuty_Count(ThisApplication.context(),
				//						business, effectivePerson, wi.getKey(), wi.getBegintime(), wi.getEndtime());
				//				wo_leads.setCount(count);
				//				wo_leads.setModuleId(WCRMModuleValues.LEADS.getWcrmId());
				//				wos.add(wo_leads);

				//商机
				count = opportunityPermissionService.getList_MyDuty_And_SubNestedDuty_Count(ThisApplication.context(),
						business, effectivePerson, _key, _begintime, _endtime);
				wo_opportunity.setCount(count);
				wo_opportunity.setModuleId(WCRMModuleValues.OPPORTUNITY.getWcrmId());
				wos.add(wo_opportunity);

				//跟进记录
				count = recordPermissonService.get_My_And_SubNested_Count(ThisApplication.context(), business,
						effectivePerson, _key, _begintime, _endtime);
				wo_record.setCount(count);
				wo_record.setModuleId(WCRMModuleValues.RECORD.getWcrmId());
				wos.add(wo_record);

			} else {
				// 人员列表，组织列表有一项或者多项不为空。根据传入的组织和人员进行计算
				List<String> _persons_p = this.convertToPerson(business, wi.getPersonNameList());
				List<String> _persons_u = this.convertToPerson(business, wi.getUnitList());
				List<String> _persons = ListTools.add(_persons_p, true, true, _persons_u);

				//客户
				count = business.customerStatisticFactory().ListByOwnerList_Count(_persons, _key, _begintime, _endtime);
				wo_customer.setCount(count);
				wo_customer.setModuleId(WCRMModuleValues.CUSTOMER.getWcrmId());
				wos.add(wo_customer);

				//联系人 
				count = business.contactsStatisticFactory().ListByOwnerList_Count(_persons, _key, _begintime, _endtime);
				wo_contacts.setCount(count);
				wo_contacts.setModuleId(WCRMModuleValues.CONTACTS.getWcrmId());
				wos.add(wo_contacts);

				//				//线索
				//				count = business.leadsFactory().ListByOwnerList_Count(_persons, wi.getKey(), wi.getBegintime(),
				//						wi.getEndtime());
				//				wo_leads.setCount(count);
				//				wo_leads.setModuleId(WCRMModuleValues.LEADS.getWcrmId());
				//				wos.add(wo_leads);

				//商机
				count = business.opportunityFactory().ListByOwnerList_Count(_persons, _key, _begintime, _endtime);
				wo_opportunity.setCount(count);
				wo_opportunity.setModuleId(WCRMModuleValues.OPPORTUNITY.getWcrmId());
				wos.add(wo_opportunity);

				//跟进记录
				count = business.recordFactory().Count_ByCreatorNameList(_persons, _key, _begintime, _endtime);
				wo_record.setCount(count);
				wo_record.setModuleId(WCRMModuleValues.RECORD.getWcrmId());
				wos.add(wo_record);
			}

			result.setData(wos);
			result.setCount((long) wos.size());
			return result;
		}
	}

	public static class Wo extends WrapOutCount {
		private String moduleId;

		public String getModuleId() {
			return moduleId;
		}

		public void setModuleId(String moduleId) {
			this.moduleId = moduleId;
		}

	}

	public static class Wi extends ListStatisticPagingWi {

	}
}
