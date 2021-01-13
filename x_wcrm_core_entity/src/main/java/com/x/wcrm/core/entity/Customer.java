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
	@Table(name = PersistenceProperties.Customer.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.Customer.table
			+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
			JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
	@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
	public class Customer extends SliceJpaObject {

	private static final long serialVersionUID = 7201459015330816780L;
	private static final String TABLE = PersistenceProperties.Customer.table;

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
		this.pinyin = StringUtils.lowerCase(PinyinHelper.convertToPinyinString(customername, "", PinyinFormat.WITHOUT_TONE));
		this.pinyinInitial = StringUtils.lowerCase(PinyinHelper.getShortPinyin(customername));
	}

	public static final String customername_FIELDNAME = "customername";
	@FieldDescribe("客户名称")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + customername_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + customername_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String customername;

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

	public static final String isLock_FIELDNAME = "isLock";
	@FieldDescribe("是否锁定，1锁定")
	@Column(length = JpaObject.length_16B, name = ColumnNamePrefix + isLock_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + isLock_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String islock;

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

	public static final String dealStatus_FIELDNAME = "dealstatus";
	@FieldDescribe("成交状态，默认未成交")
	@Column(length = JpaObject.length_128B, name = ColumnNamePrefix + dealStatus_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + dealStatus_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String dealstatus;

	public static final String telephone_FIELDNAME = "telephone";
	@FieldDescribe("固定电话号码")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + telephone_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + telephone_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String telephone;

	public static final String cellphone_FIELDNAME = "cellphone";
	@FieldDescribe("手机号码")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + cellphone_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + cellphone_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String cellphone;

	public static final String website_FIELDNAME = "website";
	@FieldDescribe("网址")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + website_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + website_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String website;

	public static final String industry_FIELDNAME = "industry";
	@FieldDescribe("所属行业")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + industry_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + industry_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String industry;

	public static final String source_FIELDNAME = "source";
	@FieldDescribe("客户来源")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + source_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + source_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String source;

	public static final String level_FIELDNAME = "level";
	@FieldDescribe("客户级别")
	@Column(name = ColumnNamePrefix + level_FIELDNAME)
	// @Index(name = TABLE + IndexNameMiddle + level_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String level;

	public static final String remark_FIELDNAME = "remark";
	@FieldDescribe("备注")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + remark_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + remark_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String remark;

	public static final String address_FIELDNAME = "address";
	@FieldDescribe("地址")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + address_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + address_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String address;

	public static final String detailAddress_FIELDNAME = "detailaddress";
	@FieldDescribe("详细地址")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + detailAddress_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + detailAddress_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String detailaddress;

	public static final String location_FIELDNAME = "location";
	@FieldDescribe("定位信息")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + location_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + location_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String location;

	public static final String lng_FIELDNAME = "lng";
	@FieldDescribe("地理位置经度")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + lng_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + lng_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String lng;

	public static final String lat_FIELDNAME = "lat";
	@FieldDescribe("地理位置维度")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + lat_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + lat_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String lat;

	public static final String province_FIELDNAME = "province";
	@FieldDescribe("省份，中文")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + province_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + province_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String province;

	public static final String city_FIELDNAME = "city";
	@FieldDescribe("地市，中文")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + city_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + city_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String city;

	// =========其他信息============
	// public static final String readonlyUserId_FIELDNAME = "readonlyUserId";
	/*
	 * @FieldDescribe("拆分工作产生的Token")
	 * 
	 * @PersistentCollection(fetch = FetchType.EAGER)
	 * 
	 * @OrderColumn(name = ORDERCOLUMNCOLUMN)
	 * 
	 * @ContainerTable(name = TABLE + ContainerTableNameMiddle +
	 * splitTokenList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle +
	 * splitTokenList_FIELDNAME + JoinIndexNameSuffix))
	 * 
	 * @ElementColumn(length = JpaObject.length_id, name = ColumnNamePrefix +
	 * splitTokenList_FIELDNAME)
	 * 
	 * @ElementIndex(name = TABLE + IndexNameMiddle + splitTokenList_FIELDNAME +
	 * ElementIndexNameSuffix)
	 * 
	 * @CheckPersist(allowEmpty = true)
	 */

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

	// =====================

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
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

	public String getIslock() {
		return islock;
	}

	public void setIslock(String islock) {
		this.islock = islock;
	}

	public Date getNexttime() {
		return nexttime;
	}

	public void setNexttime(Date nexttime) {
		this.nexttime = nexttime;
	}

	public String getDealstatus() {
		return dealstatus;
	}

	public void setDealstatus(String dealstatus) {
		this.dealstatus = dealstatus;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getFollow() {
		return follow;
	}

	public void setFollow(String follow) {
		this.follow = follow;
	}
}
