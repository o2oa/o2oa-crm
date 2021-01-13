package com.x.wcrm.assemble.control.jaxrs.common;

public enum OperationRecordType {

	CREATE("创建"),MODIFYVAL("信息修改了"),UNLOCK("解锁"),LOCK("锁定"),PUT_TO_OPENSEA("放入公海"),RECEIVE("领取"),DELIVER("转移给"),ASSIGN("分配给"),CHANGE_STATE("状态修改为");

	private String operationRecordType;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private OperationRecordType(String operationRecordType) {
		this.operationRecordType = operationRecordType;
	}

	public String getOperationRecordType() {
		return operationRecordType;
	}

	public void setOperationRecordType(String operationRecordType) {
		this.operationRecordType = operationRecordType;
	}

	// 重写 toString() 方法
	@Override
	public String toString() {
		return operationRecordType;
	}
	
	public String VAL() {
		return operationRecordType;
	}
}
