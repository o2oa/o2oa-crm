package com.x.wcrm.assemble.control.jaxrs.common;

import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.jaxrs.WrapStringList;

public class BaseAction extends StandardJaxrsAction {

	//模块Id
	public WrapStringList _getModuleIdList() {
		WrapStringList _list = new WrapStringList();
		for (WCRMModuleId value : WCRMModuleId.values()) {
			_list.addValue(value.toString(), true);
		}
		return _list;
	}

	//模块名称（中文）
	public WrapStringList _getModuleValueList() {
		WrapStringList _list = new WrapStringList();
		for (WCRMModuleValues value : WCRMModuleValues.values()) {
			_list.addValue(value.getWcrmValue(), true);
		}
		return _list;
	}

	//跟进记录类型名称
	public WrapStringList _getRecordTypeValueList() {
		WrapStringList _list = new WrapStringList();
		for (WCRMRecordType value : WCRMRecordType.values()) {
			_list.addValue(value.toString(), true);
		}
		return _list;
	}
}
