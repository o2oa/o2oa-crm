package com.x.wcrm.assemble.control.jaxrs.statistic;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;
import com.x.wcrm.core.entity.Customer;

import java.sql.Date;
import java.util.List;

public class ActionCustomerAnalyzeByAll extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(ActionCustomerAnalyzeByAll.class);

    // 所有当前用户所有递归下级的客户，按照时间倒序排列。
    ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, JsonElement jsonElement)
            throws Exception {
        try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
            ActionResult<List<Wo>> result = new ActionResult<>();
            Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
            Business business = new Business(emc);

            if (null == wi.getBegintime() || null == wi.getEndtime()) {
                throw new Exception("开始时间，结束时间不能为null");
            }
            List<Customer> os = null;
            //List<Customer> os = customerStatisticPermissionService.getNextTimeList_MyDuty(ThisApplication.context(), business, effectivePerson,
             //       adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType(), wi.getBegintime(), wi.getEndtime());
            List<Wo> wos = Wo.copier.copy(os);
            result.setData(wos);
            long count = customerStatisticPermissionService.getNextTimeCount_MyDuty(ThisApplication.context(), business, effectivePerson, wi.getKey(),
                    wi.getBegintime(), wi.getEndtime());
            result.setCount(count);

            return result;
        }
    }

    public static class Wo extends Customer {
        private static final long serialVersionUID = 5220686039082993620L;
        static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
    }

    public static class Wi extends GsonPropertyObject {
        @FieldDescribe("开始时间")
        Date begintime;

        @FieldDescribe("结束时间")
        Date endtime;

        @FieldDescribe("部门")
        private String department;

        @FieldDescribe("人员")
        private String userId;

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

        public Date getBegintime() {
            return begintime;
        }

        public void setBegintime(Date begintime) {
            this.begintime = begintime;
        }

        public Date getEndtime() {
            return endtime;
        }

        public void setEndtime(Date endtime) {
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

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
