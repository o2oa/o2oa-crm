package com.x.wcrm.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.PersistentCollection;
import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.ElementColumn;
import org.apache.openjpa.persistence.jdbc.ElementIndex;
import org.apache.openjpa.persistence.jdbc.Index;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.Storage;
import com.x.base.core.entity.StorageObject;
import com.x.base.core.entity.StorageType;
import com.x.base.core.entity.annotation.CheckPersist;
import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.project.annotation.FieldDescribe;

@ContainerEntity
@Entity
@Table(name = PersistenceProperties.Attachment.table, uniqueConstraints = {
		@UniqueConstraint(name = PersistenceProperties.Attachment.table + JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = {
				JpaObject.IDCOLUMN, JpaObject.CREATETIMECOLUMN, JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Storage(type = StorageType.report) // 先借用一下report的目录。实际应该换成wcrm

public class Attachment extends StorageObject {

	private static final long serialVersionUID = -5897208954107128132L;
	private static final String TABLE = PersistenceProperties.Attachment.table;
	private Boolean deepPath;

	@Override
	public Boolean getDeepPath() {
		return BooleanUtils.isTrue(this.deepPath);
	}

	@Override
	public void setDeepPath(Boolean deepPath) {
		this.deepPath = deepPath;
	}
	
	
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
		this.extension = StringUtils.trimToEmpty(this.extension);
		this.site = StringUtils.trimToEmpty(this.site);
	}

	public Attachment() {

	}

	public static final String wcrm_FIELDNAME = "wcrm";
	@FieldDescribe("关联的CRM业务id.")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + wcrm_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + wcrm_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String wcrm;
	
	public static final String preview_FIELDNAME = "preview";
	@FieldDescribe("预览展现方式：att|img|file|zip（att不预览，img缩略图预览，file文件预览（预留），zip压缩文件（预留））.")
	@Column(length = JpaObject.length_id, name = ColumnNamePrefix + preview_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + preview_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String preview;

	public static final String name_FIELDNAME = "name";
	@FieldDescribe("文件名称.")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + name_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + name_FIELDNAME)
	@CheckPersist(allowEmpty = false, fileNameString = true)
	private String name;

	public static final String extension_FIELDNAME = "extension";
	@FieldDescribe("扩展名。")
	@Column(length = JpaObject.length_16B, name = ColumnNamePrefix + extension_FIELDNAME)
	@CheckPersist(allowEmpty = true, fileNameString = true)
	private String extension;

	public static final String site_FIELDNAME = "site";
	@FieldDescribe("附件框分类.")
	@Column(length = JpaObject.length_64B, name = ColumnNamePrefix + site_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String site;

	public static final String crmtype_FIELDNAME = "crmType";
	@FieldDescribe("CRM的业务类型，例如：线索，客户，联系人等.")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + crmtype_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String crmType;

	public static final String workCreateTime_FIELDNAME = "workCreateTime";
	@FieldDescribe("关联的Work创建时间，用于分类目录。")
	@Column(name = ColumnNamePrefix + workCreateTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + workCreateTime_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Date workCreateTime;

	public static final String storage_FIELDNAME = "storage";
	@FieldDescribe("关联的存储名称.")
	@Column(length = JpaObject.length_64B, name = ColumnNamePrefix + storage_FIELDNAME)
	@CheckPersist(allowEmpty = false, simplyString = true)
	@Index(name = TABLE + IndexNameMiddle + storage_FIELDNAME)
	private String storage;

	public static final String length_FIELDNAME = "length";
	@FieldDescribe("文件大小.")
	@Column(name = ColumnNamePrefix + length_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + length_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Long length;

	public static final String lastUpdateTime_FIELDNAME = "lastUpdateTime";
	@FieldDescribe("最后更新时间")
	@Column(name = ColumnNamePrefix + lastUpdateTime_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + lastUpdateTime_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Date lastUpdateTime;

	public static final String lastUpdatePerson_FIELDNAME = "lastUpdatePerson";
	@FieldDescribe("最后更新人员")
	@Column(length = length_255B, name = ColumnNamePrefix + lastUpdatePerson_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + lastUpdatePerson_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String lastUpdatePerson;

	public static final String type_FIELDNAME = "type";
	@FieldDescribe("根据流文件判断的文件类型.")
	@Column(length = JpaObject.length_255B, name = ColumnNamePrefix + type_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String type;

	public static final String text_FIELDNAME = "text";
	@FieldDescribe("文本.")
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(length = JpaObject.length_100M, name = ColumnNamePrefix + text_FIELDNAME)
	@CheckPersist(allowEmpty = true)
	private String text;

	public static final String readIdentityList_FIELDNAME = "readIdentityList";
	@FieldDescribe("可以访问的身份.")
	@PersistentCollection(fetch = FetchType.EAGER)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ readIdentityList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + readIdentityList_FIELDNAME + JoinIndexNameSuffix))
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + readIdentityList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + readIdentityList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> readIdentityList;

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

	public static final String editIdentityList_FIELDNAME = "editIdentityList";
	@FieldDescribe("可以修改的用户.")
	@PersistentCollection(fetch = FetchType.EAGER)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ editIdentityList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + editIdentityList_FIELDNAME + JoinIndexNameSuffix))
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + editIdentityList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + editIdentityList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> editIdentityList;

	public static final String editUnitList_FIELDNAME = "editUnitList";
	@FieldDescribe("可以修改的组织.")
	@PersistentCollection(fetch = FetchType.EAGER)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ editUnitList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + editUnitList_FIELDNAME + JoinIndexNameSuffix))
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + editUnitList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + editUnitList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> editUnitList;

	public static final String controllerIdentityList_FIELDNAME = "controllerIdentityList";
	@FieldDescribe("可以管理的用户.")
	@PersistentCollection(fetch = FetchType.EAGER)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ controllerIdentityList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + controllerIdentityList_FIELDNAME + JoinIndexNameSuffix))
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + controllerIdentityList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + controllerIdentityList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> controllerIdentityList;

	public static final String controllerUnitList_FIELDNAME = "controllerUnitList";
	@FieldDescribe("可以管理的组织.")
	@PersistentCollection(fetch = FetchType.EAGER)
	@ContainerTable(name = TABLE + ContainerTableNameMiddle
			+ controllerUnitList_FIELDNAME, joinIndex = @Index(name = TABLE + IndexNameMiddle + controllerUnitList_FIELDNAME + JoinIndexNameSuffix))
	@OrderColumn(name = ORDERCOLUMNCOLUMN)
	@ElementColumn(length = length_255B, name = ColumnNamePrefix + controllerUnitList_FIELDNAME)
	@ElementIndex(name = TABLE + IndexNameMiddle + controllerUnitList_FIELDNAME + ElementIndexNameSuffix)
	@CheckPersist(allowEmpty = true)
	private List<String> controllerUnitList;

	@Override
	public String path() throws Exception {
		if (null == this.wcrm) {
			throw new Exception("wcrm can not be null.");
		}
		if (StringUtils.isEmpty(id)) {
			throw new Exception("id can not be empty.");
		}

		String str = "";
		str += this.crmType;
		str += PATHSEPARATOR;
		str += this.wcrm;
		str += PATHSEPARATOR;
		str += this.id;
		str += StringUtils.isEmpty(this.extension) ? "" : ("." + this.extension);
		return str;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getCrmType() {
		return crmType;
	}

	public void setCrmType(String crmType) {
		this.crmType = crmType;
	}

	public String getWcrm() {
		return wcrm;
	}

	public void setWcrm(String wcrm) {
		this.wcrm = wcrm;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public Date getWorkCreateTime() {
		return workCreateTime;
	}

	public void setWorkCreateTime(Date workCreateTime) {
		this.workCreateTime = workCreateTime;
	}

	public String getLastUpdatePerson() {
		return lastUpdatePerson;
	}

	public void setLastUpdatePerson(String lastUpdatePerson) {
		this.lastUpdatePerson = lastUpdatePerson;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getReadIdentityList() {
		return readIdentityList;
	}

	public void setReadIdentityList(List<String> readIdentityList) {
		this.readIdentityList = readIdentityList;
	}

	public List<String> getReadUnitList() {
		return readUnitList;
	}

	public void setReadUnitList(List<String> readUnitList) {
		this.readUnitList = readUnitList;
	}

	public List<String> getEditIdentityList() {
		return editIdentityList;
	}

	public void setEditIdentityList(List<String> editIdentityList) {
		this.editIdentityList = editIdentityList;
	}

	public List<String> getEditUnitList() {
		return editUnitList;
	}

	public void setEditUnitList(List<String> editUnitList) {
		this.editUnitList = editUnitList;
	}

	public List<String> getControllerIdentityList() {
		return controllerIdentityList;
	}

	public void setControllerIdentityList(List<String> controllerIdentityList) {
		this.controllerIdentityList = controllerIdentityList;
	}

	public List<String> getControllerUnitList() {
		return controllerUnitList;
	}

	public void setControllerUnitList(List<String> controllerUnitList) {
		this.controllerUnitList = controllerUnitList;
	}

}
