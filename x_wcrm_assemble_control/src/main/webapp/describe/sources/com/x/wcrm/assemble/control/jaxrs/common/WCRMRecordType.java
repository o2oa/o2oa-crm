package com.x.wcrm.assemble.control.jaxrs.common;

public enum WCRMRecordType {
	// 因为已经定义了带参数的构造器，所以在列出枚举值时必须传入对应的参数
	PHONE("打电话"), MAIL("发邮件"), SHORMESSAGE("发短信"), WEBCHAT("微信"), DINGDING("钉钉"), INTERVIEW("面谈"), ACTIVITY("活动"), BRAINWAVE("脑电波");
	// 定义一个 private 修饰的实例变量
	private String recordType;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private WCRMRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	// 重写 toString() 方法
	@Override
	public String toString() {
		return recordType;
	}
}
