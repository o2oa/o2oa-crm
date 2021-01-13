package com.x.wcrm.assemble.control.jaxrs.common;

import org.apache.commons.lang3.StringUtils;

public enum WCRMModuleValues {
	// 因为已经定义了带参数的构造器，所以在列出枚举值时必须传入对应的参数
	WCRM("客户管理", "wcrm"), LEADS("线索", "leads"), CUSTOMER("客户", "customer"), OPPORTUNITY("商机", "opportunity"), CONTACTS("联系人", "contacts"),
	RECORD("跟进记录", "record");
	// 定义一个 private 修饰的实例变量
	private String wcrmValue;
	private String wcrmId;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private WCRMModuleValues(String wcrmValue, String wcrmId) {
		this.wcrmValue = wcrmValue;
		this.wcrmId = wcrmId;
	}

	public String getWcrmValue() {
		return wcrmValue;
	}

	public void setWcrmValue(String wcrmValue) {
		this.wcrmValue = wcrmValue;
	}

	public String getWcrmId() {
		return wcrmId;
	}

	public void setWcrmId(String wcrmId) {
		this.wcrmId = wcrmId;
	}

	// 普通方法  
	public static String getIdIgnoreCase(String _wrcId) {
		for (WCRMModuleValues o : WCRMModuleValues.values()) {
			if (StringUtils.equalsAnyIgnoreCase(o.getWcrmId(), _wrcId)) {
				return o.wcrmId;
			}
		}
		return null;
	}

	public static String getValueIgnoreCase(String _wrcId) {
		for (WCRMModuleValues o : WCRMModuleValues.values()) {
			if (StringUtils.equalsAnyIgnoreCase(o.getWcrmId(), _wrcId)) {
				return o.wcrmValue;
			}
		}
		return null;
	}

	// 重写 toString() 方法
	//	@Override
	//	public String toString() {
	//		return recordType;
	//	}
}
