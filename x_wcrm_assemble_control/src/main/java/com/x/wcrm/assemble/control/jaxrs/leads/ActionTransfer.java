package com.x.wcrm.assemble.control.jaxrs.leads;

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
import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.core.entity.Leads;

//变更线索的负责人，原负责人放进ReaderList
public class ActionTransfer extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionTransfer.class);

	//	private static String REMOVE_OWNER_ONLY = "1";
	//	private static String TRANSFER_OWNER_TO_TEAMMEMBERS = "2";
	//
	//	private static String READ_ONLY = "readonly";
	//	private static String READ_WRITE = "readandwrite";

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String leadsid, JsonElement jsonElement) throws Exception {

		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();

//			Business business = new Business(emc);
			Leads o = emc.find(leadsid, Leads.class);
			// 判断leadsid是否可用
			if (null == o) {
				throw new ExceptionEntityNotExist(leadsid, Leads.class);
			}

			// 判断权限权限是否通过
			if (effectivePerson.isManager() || StringUtils.equalsIgnoreCase(effectivePerson.getDistinguishedName(), o.getOwneruser())) {
				logger.info("线索转移权限判断通过 ,uuid:" + leadsid, o);
			} else {
				throw new ExceptionLeadsNoPermission(leadsid);
			}

			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);

			// 转为团队成员（只读权限）
			String _original_OwnerUser = o.getOwneruser();
			List<String> _original_OwnerUser_List = new ArrayList<String>();
			_original_OwnerUser_List.add(_original_OwnerUser);

			String _new_distinguishName = wi.getDistinguishName();

			o.setOwneruser(_new_distinguishName);

			// 转为团队成员,read权限
			List<String> readerList = o.getReaderUserIds();
			readerList = ListTools.add(readerList, true, true, _original_OwnerUser_List);
			o.setReaderUserIds(readerList);

			emc.beginTransaction(Leads.class);
			emc.persist(o, CheckPersistType.all);
			emc.commit();
			Wo wo = Wo.copier.copy(o);
			result.setData(wo);

			operationRecordService.SaveOperationRecord(emc, effectivePerson, o, OperationRecordType.DELIVER.VAL(), _new_distinguishName);
			return result;
		}

	}

	public static class Wo extends Leads {
		private static final long serialVersionUID = -7799521234397968073L;
		static WrapCopier<Leads, Wo> copier = WrapCopierFactory.wo(Leads.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("新负责人的distinguishName")
		private String distinguishName;

		public String getDistinguishName() {
			return distinguishName;
		}

		public void setDistinguishName(String distinguishName) {
			this.distinguishName = distinguishName;
		}

	}
}
