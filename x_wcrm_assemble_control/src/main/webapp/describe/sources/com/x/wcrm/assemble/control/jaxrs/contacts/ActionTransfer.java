package com.x.wcrm.assemble.control.jaxrs.contacts;

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
import com.x.wcrm.core.entity.Contacts;

public class ActionTransfer extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionTransfer.class);

	private static String REMOVE_OWNER_ONLY = "1";
	private static String TRANSFER_OWNER_TO_TEAMMEMBERS = "2";

	private static String READ_ONLY = "readonly";
	private static String READ_WRITE = "readandwrite";

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String contactsid, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

			Business business = new Business(emc);
			Contacts o = emc.find(contactsid, Contacts.class);
			// 判断contactsid是否可用
			if (null == o) {
				throw new ExceptionEntityNotExist(contactsid, Contacts.class);
			}

			//判断公海
			boolean _isPool = contactsService.IsPool(business, contactsid);

			// 判断权限权限是否通过
			if (effectivePerson.isManager() || StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser()) || _isPool) {
				logger.info("联系人转移权限判断通过 ,uuid:" + contactsid, o);
			} else {
				throw new ExceptionContactsNoPermission(contactsid);
			}

			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			String _transferType = wi.getTransferType();

			// 判断输入数据的各种情况

			// 1类型转换标记
			if (StringUtils.isBlank(_transferType)) {
				throw new ExceptionContactsBaseMessage("转换类型不能为空。");
			} else {
				if (StringUtils.equalsIgnoreCase(_transferType, REMOVE_OWNER_ONLY)
						|| StringUtils.equalsIgnoreCase(_transferType, TRANSFER_OWNER_TO_TEAMMEMBERS)) {

				} else {
					throw new ExceptionContactsBaseMessage("转换类型不能识别。");
				}
			}
			String _new_distinguishName = wi.getDistinguishName();
			// 单纯移出
			if (StringUtils.equalsIgnoreCase(_transferType, REMOVE_OWNER_ONLY)) {
				//String _new_distinguishName = wi.getDistinguishName();
				o.setOwneruser(_new_distinguishName);
			}

			// 转为团队成员
			String _original_OwnerUser = o.getOwneruser();
			List<String> _original_OwnerUser_List = new ArrayList<String>();
			_original_OwnerUser_List.add(_original_OwnerUser);

			if (StringUtils.equalsIgnoreCase(_transferType, TRANSFER_OWNER_TO_TEAMMEMBERS)) {
				//String _new_distinguishName = wi.getDistinguishName();
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

			}

			emc.beginTransaction(Contacts.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.DELIVER.VAL(), _new_distinguishName);

			Wo wo = Wo.copier.copy(o);
			result.setData(wo);

			return result;
		}
	}

	public static class Wo extends Contacts {
		/**
		 *
		 */
		private static final long serialVersionUID = -5077668247331094075L;
		static WrapCopier<Contacts, Wo> copier = WrapCopierFactory.wo(Contacts.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("新负责人的distinguishName")
		private String distinguishName;

		@FieldDescribe("（1）单纯移出：标志\"1\" <br/> （2）转为团队成员：标志\"2\"。表示类型为字符串（暂定）")
		private String transferType;

		@FieldDescribe("（1）只读：标志\"readonly\" <br/> （2）读写：标志\"readandwrite\"。如果transferType为移出。那么这项值会被自动忽略。")
		private String readOrWrite;

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

	}

}
