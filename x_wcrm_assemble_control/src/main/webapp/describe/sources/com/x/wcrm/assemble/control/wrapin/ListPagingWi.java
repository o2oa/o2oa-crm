package com.x.wcrm.assemble.control.wrapin;

import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.gson.GsonPropertyObject;

public class ListPagingWi extends GsonPropertyObject {
	@FieldDescribe("匹配关键字")
	private String key;

	@FieldDescribe("排序字段名称")
	private String orderFieldName;

	@FieldDescribe("升序或者降序 desc或者asc")
	private String orderType;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOrderFieldName() {
		return orderFieldName;
	}

	public void setOrderFieldName(String orderFieldName) {
		this.orderFieldName = orderFieldName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

}
