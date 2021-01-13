package com.x.wcrm.assemble.control.jaxrs.attachment;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.config.StorageMapping;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoFile;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.core.entity.Attachment;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Leads;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.Record;

class ActionDownloadWithWcrm extends BaseAction {
	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, String crmId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			//			Work work = emc.find(workId, Work.class);
			//			/** 判断work是否存在 */
			//			if (null == work) {
			//				throw new ExceptionWorkNotExist(workId);
			//			}
			/** 判断attachment是否存在 */
			Attachment o = emc.find(id, Attachment.class);
			if (null == o) {
				throw new ExceptionAttachmentNotExist(id);
			}

			String crmType = "";
			crmType = o.getCrmType();
			if (StringUtils.isNoneBlank(crmType)) {
				String key = StringUtils.lowerCase(crmType);
				switch (key) {
				case "customer":
					Customer customer = emc.find(crmId, Customer.class);
					if (null == customer) {
						throw new ExceptionWcrmNotExist(crmId);
					}
					break;
				case "leads":
					Leads leads = emc.find(crmId, Leads.class);
					if (null == leads) {
						throw new ExceptionWcrmNotExist(crmId);
					}
					break;
				case "contacts":
					Contacts contacts = emc.find(crmId, Contacts.class);
					if (null == contacts) {
						throw new ExceptionWcrmNotExist(crmId);
					}
					break;
				case "record":
					Record record = emc.find(crmId, Record.class);
					if (null == record) {
						throw new ExceptionWcrmNotExist(crmId);
					}
					break;
				case "opportunity":
					Opportunity opportunity = emc.find(crmId, Opportunity.class);
					if (null == opportunity) {
						throw new ExceptionWcrmNotExist(crmId);
					}
					break;

				default:
					break;
				}
			}
			/** 生成当前用户针对work的权限控制,并判断是否可以访问 */
			//			WoControl control = business.getControl(effectivePerson, work, WoControl.class);
			//			if (BooleanUtils.isNotTrue(control.getAllowVisit())) {
			//				throw new ExceptionWorkAccessDenied(effectivePerson.getDistinguishedName(), work.getTitle(),
			//						work.getId());
			//			}
			StorageMapping mapping = ThisApplication.context().storageMappings().get(Attachment.class, o.getStorage());
			Wo wo = new Wo(o.readContent(mapping), this.contentType(false, o.getName()),
					this.contentDisposition(false, o.getName()));
			result.setData(wo);
			return result;
		}
	}

	public static class Wo extends WoFile {

		public Wo(byte[] bytes, String contentType, String contentDisposition) {
			super(bytes, contentType, contentDisposition);
		}

	}

}
