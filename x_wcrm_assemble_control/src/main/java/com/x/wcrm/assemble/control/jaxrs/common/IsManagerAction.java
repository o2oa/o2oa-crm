package com.x.wcrm.assemble.control.jaxrs.common;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;

public class IsManagerAction extends BaseAction {
    ActionResult<Wo> execute(EffectivePerson effectivePerson) throws Exception {
        try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
            ActionResult<Wo> result = new ActionResult<>();
            Wo wo = new Wo();
            Business business = new Business(emc);
            Boolean isManager = false;
            isManager = business.isManager(effectivePerson);
            wo.setManager(isManager);
            result.setData(wo);
            return result;
        }

    }

    public static class Wo {
        private Boolean isManager;
        public Boolean getManager() {
            return isManager;
        }

        public void setManager(Boolean manager) {
            isManager = manager;
        }

    }
}
