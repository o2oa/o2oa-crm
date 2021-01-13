package com.x.wcrm.assemble.control.jaxrs.customer.pool;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
import com.x.wcrm.assemble.control.jaxrs.customer.BaseAction;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;

class ActionListPoolPagingLike extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListPoolPagingLike.class);
	//	SingularAttribute<JpaObject, Date> defaultOrder = Customer_.createTime;

	// 客户分页查询，具备多个字段的like查询(无权限控制)
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement)
			throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<Customer> os = this.list(effectivePerson, business, adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(),
					wi.getOrderType());
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			result.setCount(this.count(effectivePerson, business, wi.getKey()));
			return result;

		}
	}

	private List<Customer> list(EffectivePerson effectivePerson, Business business, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType) throws Exception {
		//		SingularAttribute<JpaObject, Date> defaultOrder = Customer_.createTime;

		EntityManager em = business.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);

		Order _order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);

		List<String> matchingList = new ArrayList<>();
		matchingList.add(null);
		matchingList.add("");
		//cb.and(root.get(Customer_.owneruser).in(matchingList));

		Predicate p = cb.isNotNull(root.get(Customer_.customername));
		//p = cb.and(p, cb.isEmpty(root.get(Customer_.owneruser)));
		Predicate p_null_empty = cb.equal(root.get(Customer_.owneruser), "");
		p_null_empty = cb.or(p_null_empty, cb.isNull(root.get(Customer_.owneruser)));

		//p = cb.and(p, cb.isNull(root.get(Customer_.owneruser)));
		p = cb.and(p, p_null_empty);

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			if (StringUtils.isNotEmpty(key)) {

				p = cb.and(p,
						cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"), cb.like(root.get(Customer_.address), "%" + key + "%"),
								cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.industry), "%" + key + "%"),
								cb.like(root.get(Customer_.pinyin), "%" + key + "%"), cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"),
								cb.like(root.get(Customer_.remark), "%" + key + "%"), cb.like(root.get(Customer_.source), "%" + key + "%"),
								cb.like(root.get(Customer_.telephone), "%" + key + "%"), cb.like(root.get(Customer_.level), "%" + key + "%")));

				logger.info("ActionListPaging: like %" + key + " orderFieldName:" + orderFieldName + " orderType:" + orderType);

				if (null == _order) {
					logger.info("ActionListPaging _order is null");
					//cq.select(root).where(p).orderBy(cb.desc(root.get(defaultOrder)));
					cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
				} else {
					cq.select(root).where(p).orderBy(_order);
				}

			} else {

				if (null == _order) {
					//cq.select(root).orderBy(cb.desc(root.get(defaultOrder)));
					cq.select(root).orderBy(cb.desc(root.get(Customer_.createTime)));
				} else {
					cq.select(root).orderBy(_order);
				}

			}

		} else {
			if (null == _order) {
				//cq.select(root).orderBy(cb.desc(root.get(defaultOrder)));
				//logger.info(cb.array(arg0));
				//				logger.info("begin!!!!");
				//				logger.info("cq：" + cq.toString());
				//				logger.info("x111111111111");
				//				logger.info("root：" + root.toString());
				//				logger.info("x222222222222222");
				//				logger.info("p：" + p.toString());
				//				logger.info("x333333333333333");
				//				logger.info("cb：" + cb.toString());
				//				logger.info("x44444444444444");
				//				logger.info("defaultOrder：" + defaultOrder.toString());
				//				logger.info("end!!!");

				//cq.select(root).where(p).orderBy(cb.desc(root.get(defaultOrder)));
				cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
			} else {
				//				cq.select(root).orderBy(_order);
				cq.select(root).where(p).orderBy(_order);
			}
			// cq.select(root).orderBy(cb.desc(root.get(Leads_.createTime)));
		}
		logger.info("result cq:" + cq.toString());
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	private Long count(EffectivePerson effectivePerson, Business business, String keyString) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);

		// cq.select(cb.count(root));

		List<String> matchingList = new ArrayList<>();
		matchingList.add(null);
		matchingList.add("");
		cb.and(root.get(Customer_.owneruser).in(matchingList));

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replace(keyString, "\u3000", " "));
			if (StringUtils.isNotEmpty(key)) {
				Predicate p = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"), cb.like(root.get(Customer_.address), "%" + key + "%"),
						cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.industry), "%" + key + "%"),
						cb.like(root.get(Customer_.pinyin), "%" + key + "%"), cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"),
						cb.like(root.get(Customer_.remark), "%" + key + "%"), cb.like(root.get(Customer_.source), "%" + key + "%"),
						cb.like(root.get(Customer_.telephone), "%" + key + "%"), cb.like(root.get(Customer_.level), "%" + key + "%"),
						cb.like(root.get(Customer_.createuser), "%" + key + "%"));
				cq.select(cb.count(root)).where(p);
			} else {
				cq.select(cb.count(root));
			}
		} else {
			cq.select(cb.count(root));
		}
		return em.createQuery(cq).getSingleResult();
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
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
