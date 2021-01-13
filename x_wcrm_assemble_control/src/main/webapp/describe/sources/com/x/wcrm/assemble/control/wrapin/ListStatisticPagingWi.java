package com.x.wcrm.assemble.control.wrapin;

import java.sql.Date;
import java.util.List;

import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.gson.GsonPropertyObject;

public class ListStatisticPagingWi extends GsonPropertyObject {
	@FieldDescribe("开始时间")
	private String begintime;

	@FieldDescribe("结束时间")
	private String endtime;

	@FieldDescribe("匹配关键字")
	private String key;

	@FieldDescribe("人员的distinguishName列表")
	private List<String> personNameList;

	@FieldDescribe("组织的distinguishName列表")
	private List<String> unitList;

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

	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List<String> getPersonNameList() {
		return personNameList;
	}

	public void setPersonNameList(List<String> personNameList) {
		this.personNameList = personNameList;
	}

	public List<String> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<String> unitList) {
		this.unitList = unitList;
	}

}
