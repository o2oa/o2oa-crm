package com.x.wcrm.assemble.control.jaxrs.opportunity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.organization.Person;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Opportunity;

//根据商机id，添加团队成员（读写权限），（如果只读人员列表中存在相同人员会删除掉,如果输入的人员和责任相同返回错误）
public class ActionSetTeamWriter extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(ActionSetTeamWriter.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String opportunityid, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);

			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			List<String> _new_distinguishName_list = wi.getDistinguishName();

			Opportunity o = emc.find(opportunityid, Opportunity.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(opportunityid, Opportunity.class);
			}

			for (String _new_distinguishName : _new_distinguishName_list) {
				if (StringUtils.equalsIgnoreCase(o.getOwneruser(), _new_distinguishName)) {
					throw new ExceptionOpportunityBaseMessage(_new_distinguishName + "与 商机的负责人相同。");
				}
			}

			// 判断权限权限是否通过
			if (business.isManager(effectivePerson) || StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
				logger.info("商机转移权限判断通过 ,uuid:" + opportunityid, o);
			} else {
				throw new ExceptionOpportunityNoPermission(opportunityid);
			}

			Wo wo = new Wo();
			//			List<String> _new_distinguishName_list = new ArrayList<>();
			//			_new_distinguishName_list.add(_new_distinguishName);

			List<String> writerList = o.getWriterUserIds();
			writerList = ListTools.add(writerList, true, true, _new_distinguishName_list);
			o.setWriterUserIds(writerList);

			List<String> readerList = o.getReaderUserIds();
			for (String _new_distinguishName : _new_distinguishName_list) {
				//				if (readerList.indexOf(_new_distinguishName) >= 0) {
				//					ListTools.removeStringFromList(_new_distinguishName, readerList);
				//				}
				readerList.removeIf(s -> s.contentEquals(_new_distinguishName));
			}
			o.setReaderUserIds(readerList);

			o = opportunityService.initDefaulVal(o, effectivePerson);
			emc.beginTransaction(Opportunity.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();

			//读写
			for (String writer : writerList) {
				wo.setPerson(business.organization().person().getObject(writer));
				wo.setUnits(business.organization().unit().listWithPerson(writer));
				wo.setTeamRole(TEAMMEMBERS_ROLE);
				wo.setDispaly_permission(READWRITE_PERMISSION);

			}

			for (String reader : readerList) {
				wo.setPerson(business.organization().person().getObject(reader));
				wo.setUnits(business.organization().unit().listWithPerson(reader));
				wo.setTeamRole(TEAMMEMBERS_ROLE);
				wo.setDispaly_permission(READONLY_PERMISSION);

			}
			wo.setOpportunity(o);
			result.setData(wo);
			return result;
		}

	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("作者字符串列表")
		private List<String> distinguishName;

		public List<String> getDistinguishName() {
			return distinguishName;
		}

		public void setDistinguishName(List<String> distinguishName) {
			this.distinguishName = distinguishName;
		}

	}

	public static class Wo extends GsonPropertyObject {

		private Opportunity opportunity;
		private Person person;
		private List<String> units;

		private String teamRole;
		private String dispaly_permission;

		public Person getPerson() {
			return person;
		}

		public void setPerson(Person person) {
			this.person = person;
		}

		public List<String> getUnits() {
			return units;
		}

		public void setUnits(List<String> units) {
			this.units = units;
		}

		public String getTeamRole() {
			return teamRole;
		}

		public void setTeamRole(String teamRole) {
			this.teamRole = teamRole;
		}

		public String getDispaly_permission() {
			return dispaly_permission;
		}

		public void setDispaly_permission(String dispaly_permission) {
			this.dispaly_permission = dispaly_permission;
		}

		public Opportunity getOpportunity() {
			return opportunity;
		}

		public void setOpportunity(Opportunity opportunity) {
			this.opportunity = opportunity;
		}

	}
}
