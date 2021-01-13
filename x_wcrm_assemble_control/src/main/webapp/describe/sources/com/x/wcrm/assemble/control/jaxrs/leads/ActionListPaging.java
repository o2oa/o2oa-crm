package com.x.wcrm.assemble.control.jaxrs.leads;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.x.wcrm.core.entity.Record;
import org.apache.commons.lang3.StringUtils;

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
import com.x.wcrm.assemble.control.factory.CriteriaQueryTools;
import com.x.wcrm.core.entity.Leads;
import com.x.wcrm.core.entity.Leads_;

class ActionListPaging extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListPaging.class);
	//SingularAttribute<JpaObject, Date> defaultOrder = Leads_.createTime;

	// 线索分页查询，具备多个字段的like查询(无权限控制)
	ActionResult<List<Wo>> Execute_Paging_like(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement)
			throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<Leads> os = this.list(effectivePerson, business, adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType());
			//校验查询出的线索list是否为空
			if(!os.isEmpty()){
				//获取跟进记录中的最新跟进记录时间和跟进记录内容，并赋值到线索内容中
                leadsPermissionService.getFollowRecords(os,business);
			}

			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			result.setCount(this.count(effectivePerson, business, wi.getKey()));
			return result;
		}
	}

	private List<Leads> list(EffectivePerson effectivePerson, Business business, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Leads> cq = cb.createQuery(Leads.class);
		Root<Leads> root = cq.from(Leads.class);

		Order _order = CriteriaQueryTools.setOrder(cb, root, Leads_.class, orderFieldName, orderType);

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			if (StringUtils.isNotEmpty(key)) {

				Predicate p = cb.or(cb.like(root.get(Leads_.name), "%" + key + "%"), cb.like(root.get(Leads_.address), "%" + key + "%"),
						cb.like(root.get(Leads_.cellphone), "%" + key + "%"), cb.like(root.get(Leads_.industry), "%" + key + "%"),
						cb.like(root.get(Leads_.pinyin), "%" + key + "%"), cb.like(root.get(Leads_.pinyinInitial), "%" + key + "%"),
						cb.like(root.get(Leads_.remark), "%" + key + "%"), cb.like(root.get(Leads_.source), "%" + key + "%"),
						cb.like(root.get(Leads_.telephone), "%" + key + "%"), cb.like(root.get(Leads_.level), "%" + key + "%"),
						cb.like(root.get(Leads_.createuser), "%" + key + "%"));

				// Order _order = CriteriaQueryTools.setOrder(cb, root, Leads_.class,
				// "createTime", "desc");
				logger.info("ActionListPaging: like %" + key + " orderFieldName:" + orderFieldName + " orderType:" + orderType);

				if (null == _order) {
					logger.info("ActionListPaging _order is null");
					cq.select(root).where(p).orderBy(cb.desc(root.get(Leads_.createTime)));
				} else {
					cq.select(root).where(p).orderBy(_order);
				}

				// cq.select(root).where(p).orderBy(cb.desc(root.get(Leads_.createTime)));
			} else {

				if (null == _order) {
					cq.select(root).orderBy(cb.desc(root.get(Leads_.createTime)));
				} else {
					cq.select(root).orderBy(_order);
				}

				// cq.select(root).orderBy(cb.desc(root.get(defaultOrder)));
				// cq.select(root).orderBy(cb.desc(root.get(Leads_.createTime)));
			}

		} else {
			if (null == _order) {
				cq.select(root).orderBy(cb.desc(root.get(Leads_.createTime)));
			} else {
				cq.select(root).orderBy(_order);
			}
			// cq.select(root).orderBy(cb.desc(root.get(Leads_.createTime)));
		}
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	private Long count(EffectivePerson effectivePerson, Business business, String keyString) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Leads> root = cq.from(Leads.class);

		// cq.select(cb.count(root));

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replace(keyString, "\u3000", " "));
			if (StringUtils.isNotEmpty(key)) {
				Predicate p = cb.or(cb.like(root.get(Leads_.name), "%" + key + "%"), cb.like(root.get(Leads_.address), "%" + key + "%"),
						cb.like(root.get(Leads_.cellphone), "%" + key + "%"), cb.like(root.get(Leads_.industry), "%" + key + "%"),
						cb.like(root.get(Leads_.pinyin), "%" + key + "%"), cb.like(root.get(Leads_.pinyinInitial), "%" + key + "%"),
						cb.like(root.get(Leads_.remark), "%" + key + "%"), cb.like(root.get(Leads_.source), "%" + key + "%"),
						cb.like(root.get(Leads_.telephone), "%" + key + "%"), cb.like(root.get(Leads_.level), "%" + key + "%"),
						cb.like(root.get(Leads_.createuser), "%" + key + "%"));
				cq.select(cb.count(root)).where(p);
			} else {
				cq.select(cb.count(root));
			}
		} else {
			cq.select(cb.count(root));
		}
		return em.createQuery(cq).getSingleResult();
	}

	public static class Wo extends Leads {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Leads, Wo> copier = WrapCopierFactory.wo(Leads.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

	public static class Wi extends GsonPropertyObject {
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

}
