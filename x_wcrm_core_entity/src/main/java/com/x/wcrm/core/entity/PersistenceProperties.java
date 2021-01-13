package com.x.wcrm.core.entity;

import com.x.base.core.entity.AbstractPersistenceProperties;
import com.x.base.core.entity.JpaObject;

public final class PersistenceProperties extends AbstractPersistenceProperties {

	public static final int length_unique = JpaObject.length_255B;

	public static class WTest {
		public static final String table = "WCRM_WTest";
	}

	// 线索表
	public static class Leads {
		public static final String table = "WCRM_LEADS";
	}

	// 商机表
	public static class Opportunity {
		public static final String table = "WCRM_OPPORTUNITY";
	}

	// 商机产品关系表
	public static class Opportunity_Product {
		public static final String table = "WCRM_OPPORTUNITY_PRODUCT";
	}

	// 商机状态表
	public static class Opportunity_Status {
		public static final String table = "WCRM_OPPORTUNITY_STATUS";
	}

	// 商机状态组类别表
	public static class Opportunity_Type {
		public static final String table = "WCRM_OPPORTUNITY_TYPE";
	}

	// 联系人
	public static class Contacts {
		public static final String table = "WCRM_CONTACTS";
	}

	// 商机联系人关联表
	public static class Contacts_Opportunity {
		public static final String table = "WCRM_CONTACTS_OPPORTUNITY";
	}

	// 合同表
	public static class Contract {
		public static final String table = "WCRM_CONTRACT";
	}

	// 合同产品关系表
	public static class Contract_Product {
		public static final String table = "WCRM_CONTRACT_PRODUCT";
	}

	// 客户表
	public static class Customer {
		public static final String table = "WCRM_CUSTOMER";
	}

	// 每日客户统计
	public static class Customer_Stats {
		public static final String table = "WCRM_CUSTOMER_STATS";
	}

	// 客户规则
	public static class Customer_Setting {
		public static final String table = "WCRM_CUSTOMER_SETTING";
	}

	// 负责人变更记录表
	public static class Owner_Record {
		public static final String table = "WCRM_OWNER_RECORD";
	}

	// 产品表
	public static class Product {
		public static final String table = "WCRM_Product";
	}

	// 产品分类表
	public static class Product_Category {
		public static final String table = "WCRM_PRODUCT_CATEGORY";
	}

	// 回款表
	public static class Receivables {
		public static final String table = "WCRM_RECEIVABLES";
	}

	// 回款计划表
	public static class Receivables_Plan {
		public static final String table = "WCRM_RECEIVABLES_PLAN";
	}

	// 业绩目标
	public static class Achievement {
		public static final String table = "WCRM_ACHIEVEMENT";
	}

	// 跟进记录
	public static class Record {
		public static final String table = "WCRM_RECORD";
	}
	
	public static class  OperationRecord{
		public static final String table = "WCRM_OPERATIONRECORD";
	}

	// 本crm系统设置
	public static class WCrmConfig {
		public static final String table = "WCRM_SYSTEMCONFIG";
	}

	// 管理员设置
	public static class AdminConfig {
		public static final String table = "WCRM_ADMINCONFIG";
	}

	// 客户关系附件
	public static class Attachment {
		public static final String table = "WCRM_ATTACHMENT";
	}
}
