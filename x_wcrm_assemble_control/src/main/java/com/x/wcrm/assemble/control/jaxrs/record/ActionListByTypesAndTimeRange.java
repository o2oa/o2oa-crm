package com.x.wcrm.assemble.control.jaxrs.record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.record.ActionListByCrmId.Wo;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;
import com.x.wcrm.assemble.control.wrapout.WrapOutRecord;
import com.x.wcrm.core.entity.Record;

public class ActionListByTypesAndTimeRange extends BaseAction {

	//private static Logger logger = LoggerFactory.getLogger(ActionListByTypesAndTimeRange.class);

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String types, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			System.out.println("ActionListByTypesAndTimeRange.execute()" + types);
			Business business = new Business(emc);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Record> os = business.recordFactory().ListByTypesAndTimeRange(types, wi.getOrderFieldName(), wi.getOrderType(), sdf.parse(wi.getBegintime()),
					sdf.parse(wi.getEndtime()));

			List<Wo> wos = Wo.copier.copy(os);
			Wo.setICONBase64_byCreateuser(business, wos); //设置头像
			Wo.setRelationForList(business, wos, types); //关联WCRM对象列表
			Wo.setAttachmentList(business, wos); //设置附件列表
			result.setData(wos);
			return result;
		}
	}

	static class Wi extends ListStatisticPagingWi {
		String types;

		public String getTypes() {
			return types;
		}

		public void setTypes(String types) {
			this.types = types;
		}

	}

	public static class Wo extends WrapOutRecord {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6696967801950616996L;
		static WrapCopier<Record, Wo> copier = WrapCopierFactory.wo(Record.class, Wo.class, null, JpaObject.FieldsInvisible, false);

	}
}
