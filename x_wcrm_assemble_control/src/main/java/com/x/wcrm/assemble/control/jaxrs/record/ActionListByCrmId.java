package com.x.wcrm.assemble.control.jaxrs.record;

import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.record.ActionListByTypesAndTimeRange.Wo;
import com.x.wcrm.assemble.control.wrapout.WrapOutRecord;
import com.x.wcrm.core.entity.Record;

class ActionListByCrmId extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListByCrmId.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String crmId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Record> os = business.recordFactory().ListByCrmId(crmId);
			List<Wo> wos = Wo.copier.copy(os);
			Wo.setICONBase64_byCreateuser(business, wos); //设置人员信息，和人员头像
			Wo.setAttachmentList(business, wos); //设置附件列表

			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends WrapOutRecord {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Record, Wo> copier = WrapCopierFactory.wo(Record.class, Wo.class, null, JpaObject.FieldsInvisible);

	}

	//	public static class Wo extends Record {
	//		private Person person;
	//
	//		private String ICONBase64;
	//
	//		private static final long serialVersionUID = 1276641320278402941L;
	//		static WrapCopier<Record, Wo> copier = WrapCopierFactory.wo(Record.class, Wo.class, null,
	//				JpaObject.FieldsInvisible);
	//
	//		public Person getPerson() {
	//			return person;
	//		}
	//
	//		public void setPerson(Person person) {
	//			this.person = person;
	//		}
	//
	//		public String getICONBase64() {
	//			return ICONBase64;
	//		}
	//
	//		public void setICONBase64(String iCONBase64) {
	//			ICONBase64 = iCONBase64;
	//		}
	//
	//		public static List<Wo> setICONBase64_byCreateuser(Business business, List<Wo> wos) {
	//
	//			List<Wo> result = new ArrayList<Wo>();
	//			if (null != wos) {
	//				wos.stream().forEach(t -> {
	//					try {
	//						com.x.organization.core.entity.Person entityPerson = business.crmPersonFactory()
	//								.pick(t.getCreateuser());
	//						if (null != entityPerson) {
	//							t.setICONBase64(entityPerson.getIcon());
	//						}
	//						if (null != t.getCreateuser() && StringUtils.isNoneBlank(t.getCreateuser())) {
	//							t.setPerson(business.personFactory().getObject(t.getCreateuser()));
	//						}
	//						result.add(t);
	//					} catch (Exception e) {
	//						e.printStackTrace();
	//					}
	//				});
	//			}
	//
	//			return result;
	//		}
	//
	//	}

}
