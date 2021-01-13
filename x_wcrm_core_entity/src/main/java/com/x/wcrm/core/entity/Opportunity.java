package com.x.wcrm.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.ws.rs.DefaultValue;

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
@Table(name = PersistenceProperties.Opportunity.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.Opportunity.table
		+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
				JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Opportunity extends SliceJpaObject {

	/**
	 * 商机表
	 */
	private static final long serialVersionUID = 406995470568273912L;
	private static final String TABLE = PersistenceProperties.Opportunity.table;

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
		this.pinyin = StringUtils.lowerCase(PinyinHelper.convertToPinyinString(opportunityname, "", PinyinFormat.WITHOUT_TONE));
		this.pinyinInitial = StringUtils.lowerCase(PinyinHelper.getShortPinyin(opportunityname));
	}

	public static final String opportunityname_FIELDNAME = "opportunityname";
	@FieldDescribe("商机名称")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + opportunityname_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + opportunityname_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String opportunityname;

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

	public static final String customerId_FIELDNAME = "customerid";
	@FieldDescribe("客户ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + customerId_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + customerId_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	@DefaultValue("xx")
	private String customerid;

	public static final String typeId_FIELDNAME = "typeid";
	@FieldDescribe("商机状态组ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + typeId_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + typeId_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String typeid;

	public static final String statusId_FIELDNAME = "statusid";
	@FieldDescribe("商机状态ID（销售阶段）")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + statusId_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + statusId_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String statusid;

	public static final String statusTime_FIELDNAME = "statustime";
	@FieldDescribe("阶段推进时间")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + statusTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + statusTime_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String statustime;

	public static final String isEnd_FIELDNAME = "isend";
	@FieldDescribe("1赢单2输单3无效")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + isEnd_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + isEnd_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String isend;

	public static final String nextTime_FIELDNAME = "nexttime";
	@FieldDescribe("下次联系时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = ColumnNamePrefix + nextTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + nextTime_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Date nexttime;

	public static final String follow_FIELDNAME = "follow";
	@FieldDescribe("跟进情况（未跟进|已跟进|“”）")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + follow_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + follow_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String follow;
	
	public static final String money_FIELDNAME = "money";
	@FieldDescribe("商机金额")
	@Column(name = ColumnNamePrefix + money_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + money_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Integer money;

	public static final String totalPrice_FIELDNAME = "totalprice";
	@FieldDescribe("产品总金额")
	@Column(name = ColumnNamePrefix + totalPrice_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + totalPrice_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Integer totalprice;

	public static final String dealDate_FIELDNAME = "dealdate";
	@FieldDescribe("预计成交日期")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = ColumnNamePrefix + dealDate_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + dealDate_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Date dealdate;

	public static final String discountRate_FIELDNAME = "discountrate";
	@FieldDescribe("整单折扣")
	@Column(name = ColumnNamePrefix + totalPrice_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + totalPrice_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private Integer discountrate;

	public static final String remark_FIELDNAME = "remark";
	@FieldDescribe("备注")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + remark_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + remark_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String remark;



	//================================================

	public static final String readerUserIdList_FIELDNAME = "readerUserIdList";
	@FieldDescribe("读者列表")
	@PersistentCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ readerUserIdList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + readerUserIdList_FIELDNAME + JoinIndexNameSuffix))
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + readerUserIdList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + readerUserIdList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> readerUserIds;

	public static final String writerUserIdList_FIELDNAME = "writerUserIdList";
	@FieldDescribe("作者列表")
	@PersistentCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ writerUserIdList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + writerUserIdList_FIELDNAME + JoinIndexNameSuffix))
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + writerUserIdList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + writerUserIdList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> writerUserIds;

	public static final String createuser_FIELDNAME = "createuser";
	@FieldDescribe("创建者ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + createuser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + createuser_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String createuser;

	public static final String owneruser_FIELDNAME = "owneruser";
	@FieldDescribe("负责人ID")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + owneruser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + owneruser_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String owneruser;

	public String getOpportunityname() {
		return opportunityname;
	}

	public void setOpportunityname(String opportunityname) {
		this.opportunityname = opportunityname;
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

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getStatusid() {
		return statusid;
	}

	public void setStatusid(String statusid) {
		this.statusid = statusid;
	}

	public String getStatustime() {
		return statustime;
	}

	public void setStatustime(String statustime) {
		this.statustime = statustime;
	}

	public String getIsend() {
		return isend;
	}

	public void setIsend(String isend) {
		this.isend = isend;
	}

	public Date getNexttime() {
		return nexttime;
	}

	public void setNexttime(Date nexttime) {
		this.nexttime = nexttime;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Integer totalprice) {
		this.totalprice = totalprice;
	}

	public Date getDealdate() {
		return dealdate;
	}

	public void setDealdate(Date dealdate) {
		this.dealdate = dealdate;
	}

	public Integer getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(Integer discountrate) {
		this.discountrate = discountrate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getReaderUserIds() {
		return readerUserIds;
	}

	public void setReaderUserIds(List<String> readerUserIds) {
		this.readerUserIds = readerUserIds;
	}

	public List<String> getWriterUserIds() {
		return writerUserIds;
	}

	public void setWriterUserIds(List<String> writerUserIds) {
		this.writerUserIds = writerUserIds;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getOwneruser() {
		return owneruser;
	}

	public void setOwneruser(String owneruser) {
		this.owneruser = owneruser;
	}

	public String getFollow() {
		return follow;
	}

	public void setFollow(String follow) {
		this.follow = follow;
	}
}
