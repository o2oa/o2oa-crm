package com.x.wcrm.core.entity;

import java.util.Date;

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
@Table(name = PersistenceProperties.OperationRecord.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.OperationRecord.table
		+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
				JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class OperationRecord extends SliceJpaObject {
	private static final long serialVersionUID = 5539158100773140450L;
	/**
	 * 操作记录表
	 */
	private static final String TABLE = PersistenceProperties.OperationRecord.table;

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

	/* 以上为 JpaObject 默认字段 */

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
		//		this.pinyin = StringUtils.lowerCase(PinyinHelper.convertToPinyinString(opportunitystatusname, "", PinyinFormat.WITHOUT_TONE));
		//		this.pinyinInitial = StringUtils.lowerCase(PinyinHelper.getShortPinyin(opportunitystatusname));
	}

	public static final String createuser_FIELDNAME = "createuser";
	@FieldDescribe("创建者ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + createuser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + createuser_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String createuser;

	public static final String module_FIELDNAME = "module";
	@FieldDescribe("wcrm, leads, customer, business, contacts, record")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + module_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + module_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String module;

	public static final String crmId_FIELDNAME = "crmid";
	@FieldDescribe("关联的crmid。eg:如果是线索那么就是线索uuidid，如果是客户那么就是客户的uuid")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + crmId_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + crmId_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String crmid;

	//createTime 创建时间使用系统默认的时间。

	public static final String operationType_FIELDNAME = "operationtype";
	@FieldDescribe("操作类型，例如权限变更，内容变更，创建，领取，放入公海，锁定，结果等。具体填写什么类型还没想好。")
	@Column(name = ColumnNamePrefix + operationType_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + operationType_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String operationtype;

	public static final String content_FIELDNAME = "content";
	@FieldDescribe("内容。")
	@Column(name = ColumnNamePrefix + content_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + content_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String content;

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

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getCrmid() {
		return crmid;
	}

	public void setCrmid(String crmid) {
		this.crmid = crmid;
	}

	public String getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
