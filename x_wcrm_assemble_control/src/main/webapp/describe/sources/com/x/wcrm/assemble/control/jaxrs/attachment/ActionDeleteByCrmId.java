package com.x.wcrm.assemble.control.jaxrs.attachment;

import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.config.StorageMapping;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WrapIdList;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.core.entity.Attachment;

class ActionDeleteByCrmId extends BaseAction {

	//	private static Logger logger = LoggerFactory.getLogger(ActionDeleteByCrmId.class);

	ActionResult<WrapIdList> execute(EffectivePerson effectivePerson, String wcrmId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<WrapIdList> result = new ActionResult<>();
			Business business = new Business(emc);

			List<Attachment> os = business.attachmentFactory().listWithWcrmId(wcrmId);
			WrapIdList wrapIdList = new WrapIdList();
			for (Attachment attachment : os) {
				emc.beginTransaction(Attachment.class);
				StorageMapping mapping = ThisApplication.context().storageMappings().get(Attachment.class, attachment.getStorage());
				attachment.deleteContent(mapping);
				emc.remove(attachment);
				emc.commit();
				wrapIdList.add(attachment.getId(), false);
			}

//			Attachment attachment = emc.find(id, Attachment.class, ExceptionWhen.not_found);
//			Meeting meeting = emc.find(attachment.getMeeting(), Meeting.class, ExceptionWhen.not_found);
//			business.meetingReadAvailable(effectivePerson, meeting, ExceptionWhen.not_allow);
//			emc.beginTransaction(Attachment.class);
//			StorageMapping mapping = ThisApplication.context().storageMappings().get(Attachment.class, attachment.getStorage());
//			attachment.deleteContent(mapping);
//			emc.remove(attachment);
//			emc.commit();
//			WrapOutId wrap = new WrapOutId(meeting.getId());
//			WrapOutId wrap = new WrapOutId(attachment.getId());
			result.setData(wrapIdList);
			return result;
		}
	}

}