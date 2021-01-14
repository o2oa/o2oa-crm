package com.x.wcrm.core.entity;

import java.util.Date;

import javax.persistence.*;

import com.x.base.core.project.tools.StringTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.jdbc.Index;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.SliceJpaObject;
import com.x.base.core.entity.annotation.CheckPersist;
import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.project.annotation.FieldDescribe;
import org.apache.openjpa.persistence.jdbc.Strategy;

@Entity
@ContainerEntity
@Table(name = PersistenceProperties.Record.table, uniqueConstraints = {
		@UniqueConstraint(name = PersistenceProperties.Record.table + JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = {
				JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN, JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Record extends SliceJpaObject {

	private static final long serialVersionUID = 7201459015330816780L;
	private static final String TABLE = PersistenceProperties.Record.table;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static Long _Date2Long(Date date) {
		return date.getTime();
	}

	@FieldDescribe("数据库主键,自动生成.")
	@Id
	@Column(length = length_id, name = ColumnNamePrefix + id_FIELDNAME)
	private String id = createId();

	public void onPersist() throws Exception {
		Date date = new Date();
		if (null == this.getLongcreatetime()) {
			if (null == this.getCreateTime()) {
				this.setLongcreatetime(_Date2Long(date));
			} else {
				this.longcreatetime = _Date2Long(getCreateTime());
			}
		}
		this.longupdatetime = _Date2Long(date);
		if (StringTools.utf8Length(this.getProperties().getContent()) > length_255B) {
			this.content = StringTools.utf8SubString(this.getProperties().getContent(), length_255B - 3) + "...";
		}
	}

	public static final String types_FIELDNAME = "types";
	@FieldDescribe("关联类型")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + types_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + types_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String types;

	public static final String typesId_FIELDNAME = "typesid";
	@FieldDescribe("类型ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + typesId_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + typesId_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String typesid;

	public static final String content_FIELDNAME = "content";
	@FieldDescribe("跟进内容")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + content_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + content_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String content;

	public static final String category_FIELDNAME = "category";
	@FieldDescribe("跟进类型")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + category_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + category_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String category;

	public static final String nextTime_FIELDNAME = "nexttime";
	@FieldDescribe("下次联系时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = ColumnNamePrefix + nextTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + nextTime_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Date nexttime;

	public static final String business_ids_FIELDNAME = "businessids";
	@FieldDescribe("商机ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + business_ids_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + business_ids_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String businessids;

	public static final String contacts_ids_FIELDNAME = "contactsids";
	@FieldDescribe("联系人ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + contacts_ids_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + contacts_ids_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String contactsids;

	public static final String createuser_FIELDNAME = "createuser";
	@FieldDescribe("创建者ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + createuser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + createuser_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String createuser;

	public static final String longcreatetime_FIELDNAME = "longcreatetime";
	@FieldDescribe("创建时间的long格式存储")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + longcreatetime_FIELDNAME)
	//@Index(name = TABLE + IndexNameMiddle + longcreatetime_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Long longcreatetime;

	public static final String longupdatetime_FIELDNAME = "longupdatetime";
	@FieldDescribe("更新时间的long格式存储")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + longupdatetime_FIELDNAME)
	//@Index(name = TABLE + IndexNameMiddle + longcreatetime_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Long longupdatetime;

	public static final String properties_FIELDNAME = "properties";
	@FieldDescribe("属性对象存储字段.")
	@Persistent(fetch = FetchType.EAGER)
	@Strategy(JsonPropertiesValueHandler)
	@Column(length = JpaObject.length_10M, name = ColumnNamePrefix + properties_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private RecordProperties properties;

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getTypesid() {
		return typesid;
	}

	public void setTypesid(String typesid) {
		this.typesid = typesid;
	}

	public String getContent() {
		if (StringUtils.isNotEmpty(this.getProperties().getContent())) {
			return this.properties.getContent();
		} else {
			return this.content;
		}
	}

	public void setContent(String content) {
		this.content = content;
		this.getProperties().setContent(content);
	}

	public Record() {
		this.properties = new RecordProperties();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getNexttime() {
		return nexttime;
	}

	public void setNexttime(Date nexttime) {
		this.nexttime = nexttime;
	}

	public String getBusinessids() {
		return businessids;
	}

	public void setBusinessids(String businessids) {
		this.businessids = businessids;
	}

	public String getContactsids() {
		return contactsids;
	}

	public void setContactsids(String contactsids) {
		this.contactsids = contactsids;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Long getLongcreatetime() {
		return longcreatetime;
	}

	public void setLongcreatetime(Long longcreatetime) {
		this.longcreatetime = longcreatetime;
	}

	public Long getLongupdatetime() {
		return longupdatetime;
	}

	public void setLongupdatetime(Long longupdatetime) {
		this.longupdatetime = longupdatetime;
	}

	public RecordProperties getProperties() {
		if (null == this.properties) {
			this.properties = new RecordProperties();
		}
		return this.properties;
	}

	public void setProperties(RecordProperties properties) {
		this.properties = properties;
	}
}
