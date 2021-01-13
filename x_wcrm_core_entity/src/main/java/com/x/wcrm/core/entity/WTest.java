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
@Table(name = PersistenceProperties.WTest.table, uniqueConstraints = {
		@UniqueConstraint(name = PersistenceProperties.WTest.table + JpaObject.IndexNameMiddle
				+ JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN,
						JpaObject.CREATETIMECOLUMN, JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WTest extends SliceJpaObject {

	private static final long serialVersionUID = 2195548623059126001L;
	private static final String TABLE = PersistenceProperties.WTest.table;

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

	/* 以上为 JpaObject 默认字段 */

	public void onPersist() throws Exception {
		// this.pinyin =
		// StringUtils.lowerCase(PinyinHelper.convertToPinyinString(subject, "",
		// PinyinFormat.WITHOUT_TONE));
		// this.pinyinInitial =
		// StringUtils.lowerCase(PinyinHelper.getShortPinyin(subject));
	}

	public static final String subject_FIELDNAME = "subject";
	@FieldDescribe("名称")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + subject_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + subject_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String subject;

	public static final String remark_FIELDNAME = "remark";
	@FieldDescribe("备注")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + remark_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + remark_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String remark;

}
