package com.x.wcrm.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
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
@Table(name = PersistenceProperties.Opportunity_Status.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.Opportunity_Status.table
		+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
				JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class OpportunityStatus extends SliceJpaObject {

	private static final long serialVersionUID = 1832566746935589623L;
	/**
	 * 商机状态表
	 */
	private static final String TABLE = PersistenceProperties.Opportunity_Status.table;

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
		this.pinyin = StringUtils.lowerCase(PinyinHelper.convertToPinyinString(opportunitystatusname, "", PinyinFormat.WITHOUT_TONE));
		this.pinyinInitial = StringUtils.lowerCase(PinyinHelper.getShortPinyin(opportunitystatusname));
	}

	public static final String opportunitystatusname_FIELDNAME = "opportunitystatusname";
	@FieldDescribe("商机状态名称")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + opportunitystatusname_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + opportunitystatusname_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String opportunitystatusname;

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

	public static final String typeId_FIELDNAME = "typeid";
	@FieldDescribe("商机状态组ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + typeId_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + typeId_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String typeid;

	public static final String rate_FIELDNAME = "rate";
	@FieldDescribe("赢单率")
	@Column(name = ColumnNamePrefix + rate_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + rate_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String rate;

	public static final String orderid_FIELDNAME = "orderid";
	@FieldDescribe("排序")
	@Column(name = ColumnNamePrefix + orderid_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + orderid_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Integer orderid;

	public String getOpportunitystatusname() {
		return opportunitystatusname;
	}

	public void setOpportunitystatusname(String opportunitystatusname) {
		this.opportunitystatusname = opportunitystatusname;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

}
