package com.x.wcrm.core.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.PersistentCollection;
import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.ElementColumn;
import org.apache.openjpa.persistence.jdbc.ElementIndex;
import org.apache.openjpa.persistence.jdbc.Index;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.SliceJpaObject;
import com.x.base.core.entity.annotation.CheckPersist;
import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.project.annotation.FieldDescribe;

@Entity
@ContainerEntity
@Table(name = PersistenceProperties.Opportunity_Type.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.Opportunity_Type.table
		+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
				JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class OpportunityType extends SliceJpaObject {

	private static final long serialVersionUID = 1813293414982242885L;
	/**
	 * 商机状态组类别
	 */
	private static final String TABLE = PersistenceProperties.Opportunity_Type.table;

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
		this.pinyin = StringUtils.lowerCase(PinyinHelper.convertToPinyinString(opportunitytypename, "", PinyinFormat.WITHOUT_TONE));
		this.pinyinInitial = StringUtils.lowerCase(PinyinHelper.getShortPinyin(opportunitytypename));
	}

	public static final String opportunitytypename_FIELDNAME = "opportunitytypename";
	@FieldDescribe("商机状态组类别名称")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + opportunitytypename_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + opportunitytypename_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String opportunitytypename;

	public static final String pinyin_FIELDNAME = "pinyin";
	@FieldDescribe("name拼音.")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + pinyin_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + pinyin_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String pinyin;

	public static final String pinyinInitial_FIELDNAME = "pinyinInitial";
	@FieldDescribe("name拼音首字母.")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + pinyinInitial_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + pinyinInitial_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String pinyinInitial;

	public static final String status_FIELDNAME = "status";
	@FieldDescribe("商机状态组类别(这个字段看起来没用。就预留着吧)")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + status_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + status_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String typeid;

	public static final String readUnitList_FIELDNAME = "readUnitList";
	@FieldDescribe("可以访问的组织.")
	@PersistentCollection(fetch = FetchType.EAGER)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ readUnitList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + readUnitList_FIELDNAME + JoinIndexNameSuffix))
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + readUnitList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + readUnitList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> readUnitList;

	public static final String createuser_FIELDNAME = "createuser";
	@FieldDescribe("创建者ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + createuser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + createuser_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String createuser;

	public String getOpportunitytypename() {
		return opportunitytypename;
	}

	public void setOpportunitytypename(String opportunitytypename) {
		this.opportunitytypename = opportunitytypename;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPinyinInitial() {
		return pinyinInitial;
	}

	public void setPinyinInitial(String pinyinInitial) {
		this.pinyinInitial = pinyinInitial;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public List<String> getReadUnitList() {
		return readUnitList;
	}

	public void setReadUnitList(List<String> readUnitList) {
		this.readUnitList = readUnitList;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

}
