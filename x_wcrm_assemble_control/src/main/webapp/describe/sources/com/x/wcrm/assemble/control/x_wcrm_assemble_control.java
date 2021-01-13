package com.x.wcrm.assemble.control;

import com.x.base.core.project.Deployable;
//import com.x.base.core.entity.StorageType;
import com.x.base.core.project.annotation.Module;
import com.x.base.core.project.annotation.ModuleCategory;
import com.x.base.core.project.annotation.ModuleType;

@Module(type = ModuleType.ASSEMBLE, category = ModuleCategory.CUSTOM, name = "WCRM管理", packageName = "com.x.wcrm.assemble.control", containerEntities = {
		"com.x.wcrm.core.entity.Customer", "com.x.wcrm.core.entity.Leads", "com.x.wcrm.core.entity.Contacts", "com.x.wcrm.core.entity.ContactsAndOpportunity",
		"com.x.wcrm.core.entity.Opportunity", "com.x.wcrm.core.entity.OpportunityType", "com.x.wcrm.core.entity.OpportunityStatus",
		"com.x.wcrm.core.entity.Record", "com.x.wcrm.core.entity.OperationRecord", "com.x.wcrm.core.entity.Attachment", "com.x.wcrm.core.entity.WTest",
		"com.x.organization.core.entity.Person" }, storeJars = { "x_organization_core_entity",
				"x_organization_core_express" }, customJars = { "x_wcrm_core_entity" })
public class x_wcrm_assemble_control extends Deployable {

}
