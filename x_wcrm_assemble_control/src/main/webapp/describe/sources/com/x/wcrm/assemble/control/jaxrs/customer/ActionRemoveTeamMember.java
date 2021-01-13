package com.x.wcrm.assemble.control.jaxrs.customer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
import com.x.base.core.project.organization.Person;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Customer;

//删除团队成员，不论是在读者列表，还是作者列表中
public class ActionRemoveTeamMember extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionRemoveTeamMember.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);

			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			List<String> _new_distinguishName_list = wi.getDistinguishName();

			Customer o = emc.find(id, Customer.class);
			if (null == o) {
				throw new ExceptionEntityNotExist(id, Customer.class);
			}

			// 判断权限权限是否通过
			if (business.isManager(effectivePerson) || StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
				logger.info("客户权限判断通过 ,uuid:" + id, o);
			} else {
				throw new ExceptionCustomerNoPermission(id);
			}

			List<String> writerList = o.getWriterUserIds();
			for (String _new_distinguishName : _new_distinguishName_list) {
				writerList.removeIf(s -> s.contentEquals(_new_distinguishName));
			}
			o.setWriterUserIds(writerList);

			List<String> readerList = o.getReaderUserIds();
			for (String _new_distinguishName : _new_distinguishName_list) {
				readerList.removeIf(s -> s.contentEquals(_new_distinguishName));
			}
			o.setReaderUserIds(readerList);

			o = customerService.initDefaulVal(o, effectivePerson);

			Wo wo = new Wo();

			emc.beginTransaction(Customer.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();

			wo.setCustomer(o);
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

			result.setData(wo);
			return result;
		}

	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("一个团队成员的distinguishName的列表")
		private List<String> distinguishName;

		public List<String> getDistinguishName() {
			return distinguishName;
		}

		public void setDistinguishName(List<String> distinguishName) {
			this.distinguishName = distinguishName;
		}

	}

	public static class Wo extends GsonPropertyObject {

		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);

		private Customer customer;

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

		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

	}
}
