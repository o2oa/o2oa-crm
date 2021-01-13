package com.x.wcrm.assemble.control.jaxrs.attachment;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.config.StorageMapping;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoFile;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.core.entity.Attachment;

public class ActionGet extends BaseAction {

//	private static Logger logger = LoggerFactory.getLogger(ActionAvailable.class);

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Attachment attachment = emc.find(id, Attachment.class);

			Wo wo;
			if (null == attachment) {
				throw new ExceptionAttachmentNotExist(id);
			}
			StorageMapping mapping = ThisApplication.context().storageMappings().get(Attachment.class, attachment.getStorage());

			wo = new Wo(attachment.readContent(mapping), this.contentType(false, attachment.getName()),
					this.contentDisposition(false, attachment.getName()));
			result.setData(wo);
			return result;
		}
	}

	public static class Wo extends WoFile {

		public Wo(byte[] bytes, String contentType, String contentDisposition) {
			super(bytes, contentType, contentDisposition);
			// TODO Auto-generated constructor stub
		}

	}

	public static class WoDynamic extends Dynamic {

		private static final long serialVersionUID = -5076990764713538973L;

		public static WrapCopier<Dynamic, WoDynamic> copier = WrapCopierFactory.wo(Dynamic.class, WoDynamic.class, null, JpaObject.FieldsInvisible);

		private Long rank = 0L;

		public Long getRank() {
			return rank;
		}

		public void setRank(Long rank) {
			this.rank = rank;
		}
	}
}
