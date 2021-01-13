package com.x.wcrm.assemble.control.jaxrs.operationrecord;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
import com.x.wcrm.assemble.control.tools.DateOperation;
import com.x.wcrm.assemble.control.wrapout.WrapOutOperationRecord;
import com.x.wcrm.core.entity.OperationRecord;
import com.x.wcrm.core.entity.Record;

////根据关联的id（客户，线索，商机的uuid）列示操作记录，按照创建时间倒序排列
class ActionListByCrmId extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListByCrmId.class);
	private static final String SPACE_SYMBOL = " "; //空格

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String crmId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<OperationRecord> os = business.operationRecordFactory().ListByCrmId(crmId);
			List<Wo> wos = Wo.copier.copy(os);
			//			wos = ActionListByCrmId.ConvertDateCN(wos);

			ActionListByCrmId.ConvertDateCN(wos);	//增加一个格式化的日期
			Wo.setICONBase64_byCreateuser(business, wos);	//增加创建人信息，和创建人的base64头像
//			for (Wo wo : wos) {
//				com.x.organization.core.entity.Person entityPerson = business.crmPersonFactory()
//						.pick(wo.getCreateuser());
//				//设置头像
//				if (null != entityPerson) {
//					wo.setICONBase64(entityPerson.getIcon());
//				}
//				if (null != wo.getCreateuser() && StringUtils.isNoneBlank(wo.getCreateuser())) {
//					wo.setPerson(business.personFactory().getObject(wo.getCreateuser()));
//				}
//			}

			result.setData(wos);
			return result;
		}
	}

	//补充一个 日期格式:2019-10-17 星期四
	public static List<Wo> ConvertDateCN(List<Wo> wos) {
		DateOperation DateOperation = new DateOperation();
		String _dateStr = "";
		for (Wo wo : wos) {
			Date _createTime = wo.getCreateTime();
			_dateStr = DateOperation.getDate(_createTime);
			_dateStr = _dateStr + SPACE_SYMBOL + DateOperation.dateToWeek(_dateStr);
			wo.setDateCN(_dateStr);
		}
		return wos;
	}

	public static class Wo extends WrapOutOperationRecord {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9182807830221600256L;
		static WrapCopier<OperationRecord, Wo> copier = WrapCopierFactory.wo(OperationRecord.class, Wo.class, null,
				JpaObject.FieldsInvisible);

	}

	//	public static class Wo extends OperationRecord {
	//		private static final long serialVersionUID = 8840922121357474254L;
	//
	//		private Person person;
	//
	//		private String ICONBase64; //头像的base64
	//
	//		private String DateCN; //增加一个日期格式 ：2019-10-17 星期四
	//
	//		static WrapCopier<OperationRecord, Wo> copier = WrapCopierFactory.wo(OperationRecord.class, Wo.class, null, JpaObject.FieldsInvisible);
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
	//		public String getDateCN() {
	//			return DateCN;
	//		}
	//
	//		public void setDateCN(String dateCN) {
	//			DateCN = dateCN;
	//		}
	//
	//	}
}
