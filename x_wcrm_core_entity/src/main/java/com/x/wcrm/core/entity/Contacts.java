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
@Table(name = PersistenceProperties.Contacts.table, uniqueConstraints = { @UniqueConstraint(name = PersistenceProperties.Contacts.table
		+ JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN,
				JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Contacts extends SliceJpaObject {

	private static final long serialVersionUID = 7201459015330816780L;
	private static final String TABLE = PersistenceProperties.Contacts.table;

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
		this.pinyin = StringUtils.lowerCase(PinyinHelper.convertToPinyinString(contactsname, "", PinyinFormat.WITHOUT_TONE));
		this.pinyinInitial = StringUtils.lowerCase(PinyinHelper.getShortPinyin(contactsname));
	}

	public static final String contactsname_FIELDNAME = "contactsname";
	@FieldDescribe("联系人名称")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + contactsname_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + contactsname_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String contactsname;

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
	@CheckPersist(allowEmpty = false)
	private String customerid;

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

	public static final String email_FIELDNAME = "email";
	@FieldDescribe("电子邮箱")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + email_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + email_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String email;

	public static final String decision_FIELDNAME = "decision";
	@FieldDescribe("是否关键决策人")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + decision_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + decision_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String decision;

	public static final String post_FIELDNAME = "post";
	@FieldDescribe("职务")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + post_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + post_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String post;

	public static final String sex_FIELDNAME = "sex";
	@FieldDescribe("性别")
	@Column(length = JpaObject.length_128B, name = ColumnNamePrefix + sex_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + sex_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String sex;

	public static final String detailAddress_FIELDNAME = "detailaddress";
	@FieldDescribe("详细地址")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + detailAddress_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + detailAddress_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String detailaddress;

	public static final String province_FIELDNAME = "province";
	@FieldDescribe("所在地区")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + province_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + province_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String province;

	public static final String remark_FIELDNAME = "remark";
	@FieldDescribe("备注")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + remark_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + remark_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String remark;

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

	// ===========================================================================================
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
	@FieldDescribe("创建者distinguishName")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + createuser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + createuser_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String createuser;

	public static final String owneruser_FIELDNAME = "owneruser";
	@FieldDescribe("负责人distinguishName")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + owneruser_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + owneruser_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String owneruser;

	public String getContactsname() {
		return contactsname;
	}

	public void setContactsname(String contactsname) {
		this.contactsname = contactsname;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getNexttime() {
		return nexttime;
	}

	public void setNexttime(Date nexttime) {
		this.nexttime = nexttime;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
