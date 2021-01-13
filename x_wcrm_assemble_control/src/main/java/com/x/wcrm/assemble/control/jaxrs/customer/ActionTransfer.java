package com.x.wcrm.assemble.control.jaxrs.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.assemble.control.jaxrs.common.WCRMModuleId;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Opportunity;

// 客户转移
/*
 * 1.变更负责人为新的负责人 _distinguishName 2.将原负责人：1)单纯移出，2)或者转为团队成员(只读，读写2种权限) 3.同时变更负责人至：联系人，商机，合同（未实现）
 */
public class ActionTransfer extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionTransfer.class);

	// private static String REMOVE_OWNER_ONLY = "remove_owner_only";
	// private static String TRANSFER_OWNER_TO_TEAMMEMBERS = "transfer_owner_to_teammembers";

	private static String REMOVE_OWNER_ONLY = "1";
	private static String TRANSFER_OWNER_TO_TEAMMEMBERS = "2";

	private static String READ_ONLY = "readonly";
	private static String READ_WRITE = "readandwrite";

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String customerid, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

			Business business = new Business(emc);
			Customer o = emc.find(customerid, Customer.class);
			// 判断customerid是否可用
			if (null == o) {
				throw new ExceptionEntityNotExist(customerid, Customer.class);
			}

			// 判断权限权限是否通过
			if (effectivePerson.isManager() || StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
				logger.info("客户转移权限判断通过 ,uuid:" + customerid, o);
			} else {
				throw new ExceptionCustomerNoPermission(customerid);
			}

			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			String _transferType = wi.getTransferType();

			// 判断输入数据的各种情况

			// 1类型转换标记
			if (StringUtils.isBlank(_transferType)) {
				throw new ExceptionCustomerBaseMessage("转换类型不能为空。");
			} else {
				if (StringUtils.equalsIgnoreCase(_transferType, REMOVE_OWNER_ONLY)
						|| StringUtils.equalsIgnoreCase(_transferType, TRANSFER_OWNER_TO_TEAMMEMBERS)) {

				} else {
					throw new ExceptionCustomerBaseMessage("转换类型不能识别。");
				}
			}

			String _new_distinguishName = wi.getDistinguishName();
			emc.beginTransaction(Customer.class);
			emc.beginTransaction(Contacts.class);
			emc.beginTransaction(Opportunity.class);
			// 单纯移出
			if (StringUtils.equalsIgnoreCase(_transferType, REMOVE_OWNER_ONLY)) {
				//				String _new_distinguishName = wi.getDistinguishName();
				o.setOwneruser(_new_distinguishName);
				//包含联系人
				if (ListTools.contains(wi.getRelationTypeList(),WCRMModuleId.contacts.toString())) {
					List<Contacts> os = contactsService.ListByCustomerId(emc, business, customerid);
					contactsService.remove_owner_only(emc, effectivePerson, os, _new_distinguishName);
				}

				//包含商机
				if (ListTools.contains(wi.getRelationTypeList(),WCRMModuleId.opportunity.toString())) {
					List<Opportunity> os = opportunityService.ListByCustomerId(emc, business, customerid);
					opportunityService.remove_owner_only(emc, effectivePerson, os, _new_distinguishName);
				}

			}

			// 转为团队成员
			String _original_OwnerUser = o.getOwneruser();
			List<String> _original_OwnerUser_List = new ArrayList<String>();
			_original_OwnerUser_List.add(_original_OwnerUser);

			if (StringUtils.equalsIgnoreCase(_transferType, TRANSFER_OWNER_TO_TEAMMEMBERS)) {
				//				String _new_distinguishName = wi.getDistinguishName();

				o.setOwneruser(_new_distinguishName);

				// 转为团队成员,read权限
				if (StringUtils.equalsIgnoreCase(wi.getReadOrWrite(), READ_ONLY)) {
					List<String> readerList = o.getReaderUserIds();
					readerList = ListTools.add(readerList, true, true, _original_OwnerUser_List);
					o.setReaderUserIds(readerList);
				}

				// 转为团队成员,read && write 权限
				if (StringUtils.equalsIgnoreCase(wi.getReadOrWrite(), READ_WRITE)) {
					List<String> writeList = o.getReaderUserIds();
					writeList = ListTools.add(writeList, true, true, _original_OwnerUser_List);
					o.setWriterUserIds(writeList);
				}

				//包含联系人
				if (ListTools.contains(wi.getRelationTypeList(),WCRMModuleId.contacts.toString())) {
					List<Contacts> os = contactsService.ListByCustomerId(emc, business, customerid);
					contactsService.transfer_owner_to_teammembers(emc, effectivePerson, os, _new_distinguishName, wi.getReadOrWrite());
				}

				//包含商机
				if (ListTools.contains(wi.getRelationTypeList(),WCRMModuleId.opportunity.toString())) {
					List<Opportunity> os = opportunityService.ListByCustomerId(emc, business, customerid);
					opportunityService.transfer_owner_to_teammembers(emc, effectivePerson, os, _new_distinguishName, wi.getReadOrWrite());
				}

			}

			//emc.persist(o, CheckPersistType.all);
			emc.check( o , CheckPersistType.all );
			emc.commit();
			Wo wo = Wo.copier.copy(o);
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.DELIVER.VAL(), _new_distinguishName);
			return result;
		}
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = -5323397603785870231L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("新负责人的distinguishName")
		private String distinguishName;

		@FieldDescribe("（1）单纯移出：标志\"1\" <br/> （2）转为团队成员：标志\"2\"。表示类型为字符串（暂定）")
		private String transferType;

		@FieldDescribe("（1）只读：标志\"readonly\" <br/> （2）读写：标志\"readandwrite\"。如果transferType为移出。那么这项值会被自动忽略。")
		private String readOrWrite;

		@FieldDescribe("listModuleId中的，contacts或者opportunity")
		private List<String> relationTypeList;

		public String getDistinguishName() {
			return distinguishName;
		}

		public void setDistinguishName(String distinguishName) {
			this.distinguishName = distinguishName;
		}

		public String getTransferType() {
			return transferType;
		}

		public void setTransferType(String transferType) {
			this.transferType = transferType;
		}

		public String getReadOrWrite() {
			return readOrWrite;
		}

		public void setReadOrWrite(String readOrWrite) {
			this.readOrWrite = readOrWrite;
		}

		public List<String> getRelationTypeList() {
			return relationTypeList;
		}

		public void setRelationTypeList(List<String> relationTypeList) {
			this.relationTypeList = relationTypeList;
		}

	}
}
