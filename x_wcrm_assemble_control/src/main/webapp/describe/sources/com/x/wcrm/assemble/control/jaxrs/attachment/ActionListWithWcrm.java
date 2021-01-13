package com.x.wcrm.assemble.control.jaxrs.attachment;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Attachment;

class ActionListWithWcrm extends BaseAction {
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String workId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
//			Work work = emc.find(workId, Work.class);
//			/** 判断work是否存在 */
//			if (null == work) {
//				throw new ExceptionWorkNotExist(workId);
//			}
//			WoControl control = business.getControl(effectivePerson, work, WoControl.class);
//			if (BooleanUtils.isNotTrue(control.getAllowVisit())) {
//				throw new ExceptionWorkAccessDenied(effectivePerson.getDistinguishedName(), work.getTitle(),
//						work.getId());
//			}
			List<Attachment> os = business.attachmentFactory().listWithWcrmId(workId);
			
			List<Wo> wos = Wo.copier.copy(os);
			wos = business.attachmentFactory().sort(wos);
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Attachment {

		private static final long serialVersionUID = 1954637399762611493L;

		static WrapCopier<Attachment, Wo> copier = WrapCopierFactory.wo(Attachment.class, Wo.class, null, Wo.Excludes,
				true);

		public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

		private Long rank;

		public Long getRank() {
			return rank;
		}

		public void setRank(Long rank) {
			this.rank = rank;
		}

	}

//	public static class WoControl extends WorkControl {
//	}

}
