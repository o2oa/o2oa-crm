package com.x.wcrm.assemble.control.service;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleValues;
import com.x.wcrm.core.entity.OperationRecord;

public class OperationRecordService {

	private static Logger logger = LoggerFactory.getLogger(OperationRecordService.class);
	private static final String LE = "了";
	private static final String JIANG = "将";

	// 初始化信息
	public OperationRecord initDefaultValue(EffectivePerson effectivePerson, OperationRecord operationRecord) {
		// 补全默认创建人
		if (null == operationRecord.getCreateuser() || StringUtils.isBlank(operationRecord.getCreateuser())) {
			operationRecord.setCreateuser(effectivePerson.getDistinguishedName());
		}

		return operationRecord;
	}

	//保存操作记录
	/*	public void SaveOperationRecord(EntityManagerContainer emc, EffectivePerson effectivePerson, Object _crmTypeObject, String _operationType)
				throws Exception {
	
			String _typeSimpleName = _crmTypeObject.getClass().getSimpleName();
	
			OperationRecord o = new OperationRecord();
			Class<?> clz = Class.forName(_crmTypeObject.getClass().getTypeName());
			//		Object obj = clz.newInstance();
			//		String _id = (String) m.invoke(obj, null);
			Method m = clz.getMethod("getId", null);
			String _id = (String) m.invoke(_crmTypeObject, null);
			logger.info("保存操作记录 _id:" + _id);
			o.setCrmid(_id);
			o.setCreateuser(effectivePerson.getDistinguishedName());
			//String _moduleId = StringUtils.lowerCase(_crmTypeObject.getClass().getSimpleName());
			String _moduleId = WCRMModuleValues.getIdIgnoreCase(_typeSimpleName);
			o.setModule(WCRMModuleValues.getIdIgnoreCase(_moduleId));
			o.setContent("创建一个用户.");
			o.setOperationtype(_operationType);
			// 初始化默认值
			// ActionCreate.initDefaultValue(effectivePerson, o);
			// contactsService.initDefaultValue(effectivePerson, o);
	
			emc.beginTransaction(OperationRecord.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
	
		}*/
	//保存操作记录
	public void SaveOperationRecord(EntityManagerContainer emc, EffectivePerson effectivePerson, Object _crmTypeObject, String _operationType,
			String... persons) throws Exception {
		OperationRecord o = new OperationRecord();
		String _typeSimpleName = ClassUtils.getShortClassName(_crmTypeObject.getClass());
		String _id = (String) MethodUtils.invokeExactMethod(_crmTypeObject, "getId");
		logger.info("保存操作记录 _id:" + _id);
		o.setCrmid(_id);
		o.setCreateuser(effectivePerson.getDistinguishedName());

		//crm中子类型的id
		String _moduleId = WCRMModuleValues.getIdIgnoreCase(_typeSimpleName);
		o.setModule(WCRMModuleValues.getIdIgnoreCase(_moduleId));

		String _moduleName = WCRMModuleValues.getValueIgnoreCase(_moduleId);
		String _content = "";

		//可变参数判断
		if (persons == null || persons.length <= 0) {
			_content = combineOperationRecordContent(emc, effectivePerson, _moduleName, _operationType, null);
		} else {
			if (persons.length > 0) {
				String _personString = "";
				for (String person : persons) {
					if (StringUtils.isBlank(_personString)) {
						_personString = person;
					} else {
						_personString += "," + person;
					}
				}
				_content = combineOperationRecordContent(emc, effectivePerson, _moduleName, _operationType, _personString);
			} else {
				_content = combineOperationRecordContent(emc, effectivePerson, _moduleName, _operationType, null);
			}
		}

		//o.setContent("创建一个用户.");
		o.setContent(_content);
		o.setOperationtype(_operationType);

		emc.beginTransaction(OperationRecord.class);
		emc.persist(o, CheckPersistType.all);
		emc.commit();
	}

	private static String combineOperationRecordContent(EntityManagerContainer emc, EffectivePerson effectivePerson, String _moduleName, String _operationType,
			String personName) {
		String content = "";
		//创建
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.CREATE.VAL())) {
			content = operationRecordContentFormmat_LE(OperationRecordType.CREATE.VAL(), _moduleName);
		}
		//领取
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.RECEIVE.VAL())) {
			content = operationRecordContentFormmat_LE(OperationRecordType.RECEIVE.VAL(), _moduleName);
		}

		//放入公海
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.PUT_TO_OPENSEA.VAL())) {
			content = operationRecordContentFormmat_JIANG(_operationType, _moduleName);
		}

		//解锁与锁定
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.LOCK.VAL())
				|| StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.UNLOCK.VAL())) {
			content = operationRecordContentFormmat_JIANG(_operationType, _moduleName);
		}

		//转移
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.DELIVER.VAL())) {
			//将客户转移给：张倩倩
			if (StringUtils.isNotEmpty(personName) && StringUtils.isNoneBlank(personName)) {
				//content = JIANG + _moduleName + OperationRecordType.DELIVER.VAL() + strings[0];
				content = operationRecordContentFormmat_JIANG_TO_somePerson(_operationType, _moduleName, personName);
			} else {
				content = operationRecordContentFormmat_JIANG_TO_somePerson(_operationType, _moduleName, "");
			}
		}

		//分配
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.ASSIGN.VAL())) {
			//将客户转移给：张倩倩
			if (StringUtils.isNotEmpty(personName) && StringUtils.isNoneBlank(personName)) {
				content = operationRecordContentFormmat_JIANG_TO_somePerson(_operationType, _moduleName, personName);
			} else {
				content = operationRecordContentFormmat_JIANG_TO_somePerson(_operationType, _moduleName, "");
			}
		}

		//改变状态
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.CHANGE_STATE.VAL())) {
			//将客户状态修改为：已成交
			if (StringUtils.isNotEmpty(personName) && StringUtils.isNoneBlank(personName)) {
				content = operationRecordContentFormmat_JIANG_TO_somePerson(_operationType, _moduleName, personName);
			} else {
				content = operationRecordContentFormmat_JIANG_TO_somePerson(_operationType, _moduleName, "");
			}
		}
		//修改内容
		if (StringUtils.equalsIgnoreCase(_operationType, OperationRecordType.MODIFYVAL.VAL())) {
			content = operationRecordContentFormmat_JIANG(_operationType, _moduleName);
		}

		return content;
	}

	//eg：{_operationType}了{_moduleName}
	private static String operationRecordContentFormmat_LE(String _operationType, String _moduleName) {
		String formmatContent = "";
		formmatContent = _operationType + LE + _moduleName;
		return formmatContent;
	}

	//eg：将{_moduleName}{_operationType}
	private static String operationRecordContentFormmat_JIANG(String _operationType, String _moduleName) {
		String formmatContent = "";
		formmatContent = JIANG + _moduleName + _operationType;
		return formmatContent;
	}

	//eg：将{_moduleName}{_operationType}:{personName}
	private static String operationRecordContentFormmat_JIANG_TO_somePerson(String _operationType, String _moduleName, String personName) {
		String formmatContent = "";
		formmatContent = JIANG + _moduleName + _operationType + ":" + personName;
		return formmatContent;
	}

}
