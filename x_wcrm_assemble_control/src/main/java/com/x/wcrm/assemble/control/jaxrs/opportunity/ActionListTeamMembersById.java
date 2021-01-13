package com.x.wcrm.assemble.control.jaxrs.opportunity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
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

//根据id，列出相关团队成员：负责人--团队成员（读写权限）--团队成员（只读权限）
public class ActionListTeamMembersById extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(ActionListTeamMembersById.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();

			Business business = new Business(emc);

			Opportunity o = emc.find(id, Opportunity.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Opportunity.class);
			}

			// 判断权限权限是否通过
			if (effectivePerson.isManager() || StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
				logger.info("商机权限判断通过 ,uuid:" + id, o);
			} else {
				throw new ExceptionOpportunityNoPermission(id);
			}

			List<Wo> wos = new ArrayList<>();
			Wo wo = new Wo();

			//负责人
			String _owneruser = o.getOwneruser();
			//			if (StringUtils.isBlank(_owneruser)) {
			//				throw new ExceptionOpportunityBaseMessage("负责人为空");
			//			}

			if (StringUtils.isBlank(_owneruser) || StringUtils.isEmpty(_owneruser)) {
				Person tmpPerson = new Person();
				tmpPerson.setName("");
				wo.setPerson(tmpPerson);
				List<String> tmpUnits = new ArrayList<>();
				wo.setUnits(tmpUnits);
				wo.setTeamRole("");
				wo.setDispaly_permission("");
				wos.add(wo);
			} else {
				wo.setPerson(business.organization().person().getObject(_owneruser));
				wo.setUnits(business.organization().unit().listWithPerson(_owneruser));
				wo.setTeamRole(OWNER_ROLE);
				wo.setDispaly_permission(OWNER_PERMISSION);
				wos.add(wo);
			}

			//orgTest(business, _owneruser);

			List<String> writerList = o.getWriterUserIds();
			ListTools.removeStringFromList(_owneruser, writerList);

			List<String> readerList = o.getReaderUserIds();
			ListTools.removeStringFromList(_owneruser, readerList);

			//读写
			if (!writerList.isEmpty() && writerList.size() > 0) {
				for (String writer : writerList) {
					Wo wo_write = new Wo();
					wo_write.setPerson(business.organization().person().getObject(writer));
					wo_write.setUnits(business.organization().unit().listWithPerson(writer));
					wo_write.setTeamRole(TEAMMEMBERS_ROLE);
					wo_write.setDispaly_permission(READWRITE_PERMISSION);
					wos.add(wo_write);
				}
			}

			if (!readerList.isEmpty() && readerList.size() > 0) {
				for (String reader : readerList) {
					Wo wo_read = new Wo();
					wo_read.setPerson(business.organization().person().getObject(reader));
					wo_read.setUnits(business.organization().unit().listWithPerson(reader));
					wo_read.setTeamRole(TEAMMEMBERS_ROLE);
					wo_read.setDispaly_permission(READONLY_PERMISSION);
					wos.add(wo_read);
				}
			}

			//wos.add(wo);
			result.setData(wos);
			return result;
		}

	}

	public static class Wo extends GsonPropertyObject implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 4868126829599987961L;
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

	}
}
