package com.x.wcrm.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.openjpa.persistence.jdbc.Index;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.SliceJpaObject;
import com.x.base.core.entity.annotation.CheckPersist;
import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.project.annotation.FieldDescribe;

@Entity
@ContainerEntity
@Table(name = PersistenceProperties.Contacts_Opportunity.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.Contacts_Opportunity.table
		+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
				JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ContactsAndOpportunity extends SliceJpaObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4818036666892712097L;
	/*
	 * *联系人商机关系
	 */
	private static final String TABLE = PersistenceProperties.Contacts_Opportunity.table;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@FieldDescribe("数据库主键,自动生成.")
	@Id
	@Column(length = length_id, name = ColumnNamePrefix + id_FIELDNAME)
	private String id = createId();

	public void onPersist() throws Exception {
	}

	/* 以上为 JpaObject 默认字段 */

	public static final String contactsid_FIELDNAME = "contactsid";
	@FieldDescribe("联系人UUID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + contactsid_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + contactsid_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String contactsid;

	public static final String opportunityid_FIELDNAME = "opportunityid";
	@FieldDescribe("商机UUID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + opportunityid_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + opportunityid_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String opportunityid;

	public String getContactsid() {
		return contactsid;
	}

	public void setContactsid(String contactsid) {
		this.contactsid = contactsid;
	}

	public String getOpportunityid() {
		return opportunityid;
	}

	public void setOpportunityid(String opportunityid) {
		this.opportunityid = opportunityid;
	}

}
