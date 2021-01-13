package com.x.wcrm.assemble.control.wrapout;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.organization.Person;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleValues;
import com.x.wcrm.core.entity.Attachment;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Leads;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.Record;

public class WrapOutRecord extends Record {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1049919158673898006L;

	public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

	public static WrapCopier<Record, WrapOutRecord> copier_toWrapOutRecord = WrapCopierFactory.wo(Record.class, WrapOutRecord.class, null,
			JpaObject.FieldsInvisible);

	private Long rank;

	private Person person;

	private String ICONBase64;

	@FieldDescribe("普通附件（不预览）")
	private List<Attachment> attachmentList;

	private List<Attachment> attachmentListPreview;

	private Leads leads;

	private Customer customer;

	private Contacts contacts;

	private Opportunity opportunity;

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getICONBase64() {
		return ICONBase64;
	}

	public void setICONBase64(String iCONBase64) {
		ICONBase64 = iCONBase64;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<Attachment> getAttachmentListPreview() {
		return attachmentListPreview;
	}

	public void setAttachmentListPreview(List<Attachment> attachmentListPreview) {
		this.attachmentListPreview = attachmentListPreview;
	}

	public Leads getLeads() {
		return leads;
	}

	public void setLeads(Leads leads) {
		this.leads = leads;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Contacts getContacts() {
		return contacts;
	}

	public void setContacts(Contacts contacts) {
		this.contacts = contacts;
	}

	public Opportunity getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(Opportunity opportunity) {
		this.opportunity = opportunity;
	}

	/**
	 * 设置附件()
	 */
	public static List<? super WrapOutRecord> setAttachmentList(Business business, List<? extends WrapOutRecord> wos) {

		List<? super WrapOutRecord> result = new ArrayList<WrapOutRecord>();
		if (null != wos) {
			wos.stream().forEach(t -> {
				try {
					//预览的图片
					List<Attachment> attListPreview = business.attachmentFactory().listWithWcrmId_PreviewType(t.getId(), "img");
					t.setAttachmentListPreview(attListPreview);
					List<Attachment> attList = business.attachmentFactory().listWithWcrmId_PreviewType(t.getId(), "att");
					t.setAttachmentList(attList);
					result.add(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		return result;
	}

	/**
	 * 设置创建人员的 人员对象信息，和头像的base64
	 */
	public static List<? super WrapOutRecord> setICONBase64_byCreateuser(Business business, List<? extends WrapOutRecord> wos) {

		List<? super WrapOutRecord> result = new ArrayList<WrapOutRecord>();
		if (null != wos) {
			wos.stream().forEach(t -> {
				try {
					com.x.organization.core.entity.Person entityPerson = business.crmPersonFactory().pick(t.getCreateuser());
					if (null != entityPerson) {
						String _IconLdpi = entityPerson.getIconLdpi();
						if (null == _IconLdpi || StringUtils.isEmpty(_IconLdpi) || StringUtils.isBlank(_IconLdpi)) {
							t.setICONBase64(entityPerson.getIcon());
						} else {
							t.setICONBase64(_IconLdpi);
						}
						//t.setICONBase64(entityPerson.getIconLdpi());

					}
					if (null != t.getCreateuser() && StringUtils.isNoneBlank(t.getCreateuser())) {
						t.setPerson(business.personFactory().getObject(t.getCreateuser()));
					}
					result.add(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		return result;
	}

	/**
	 * 设置跟进记录关联的客户，商机，联系人，线索。 每次关联一种类型
	 * 
	 * @param types 为：leads,customer,opportunity,contacts
	 */
	public static List<? super WrapOutRecord> setRelationForList(Business business, List<? extends WrapOutRecord> wos, String types) {

		List<? super WrapOutRecord> result = new ArrayList<WrapOutRecord>();
		if (null != wos) {
			types = StringUtils.lowerCase(types);

			//客户
			if (StringUtils.equals(types, WCRMModuleValues.CUSTOMER.getWcrmId())) {
				wos.stream().forEach(t -> {
					try {
						Customer o = business.customerFactory().get(t.getTypesid());
						if (null != o) {
							t.setCustomer(o);
						}
						result.add(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

			//线索
			if (StringUtils.equals(types, WCRMModuleValues.LEADS.getWcrmId())) {
				wos.stream().forEach(t -> {
					try {
						Leads o = business.leadsFactory().get(t.getTypesid());
						if (null != o) {
							t.setLeads(o);
						}
						result.add(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

			//联系人
			if (StringUtils.equals(types, WCRMModuleValues.CONTACTS.getWcrmId())) {
				wos.stream().forEach(t -> {
					try {
						Contacts o = business.contactsFactory().get(t.getTypesid());
						if (null != o) {
							t.setContacts(o);
						}
						result.add(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

			//商机
			if (StringUtils.equals(types, WCRMModuleValues.OPPORTUNITY.getWcrmId())) {
				wos.stream().forEach(t -> {
					try {
						Opportunity o = business.opportunityFactory().get(t.getTypesid());
						if (null != o) {
							t.setOpportunity(o);
						}
						result.add(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

		}

		return result;
	}
}
