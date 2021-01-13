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
import com.x.wcrm.core.entity.Record;

class ActionList extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionList.class);

	// 先取出所有id，根据id列出所有线索
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<String> ids = business.recordFactory().fetchAllIds();
			List<Wo> wos = Wo.copier.copy(emc.list(Record.class, ids));
			result.setData(wos);
			return result;
		}
	}

	// 列出创建是当前登录用户的所有线索,并且按照创建时间倒序排列。
	ActionResult<List<Wo>> MyExecute(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<String> ids = business.recordFactory().fetchAllIdsByCreator(effectivePerson.getDistinguishedName());
			List<Wo> wos = Wo.copier.copy(emc.list(Record.class, ids));
			result.setData(wos);
			return result;
		}
	}

	// 所有线索，按照时间倒序排列。
	ActionResult<List<Wo>> Execute_OrderByCreateTime(EffectivePerson effectivePerson) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Record> os = business.recordFactory().fetchAll();
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Record {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Record, Wo> copier = WrapCopierFactory.wo(Record.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
