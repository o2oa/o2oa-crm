package com.x.wcrm.assemble.control.jaxrs.customer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.x.wcrm.core.entity.Contacts_;
import com.x.wcrm.core.entity.Record_;
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
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;

class ActionListPagingLike extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListPagingLike.class);

	// 客户分页查询，具备多个字段的like查询(无权限控制)
	ActionResult<List<Wo>> Execute_Paging_like(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement)
			throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<Customer> os = this.list(effectivePerson, business, adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType(),wi.getYearMonth(),wi.getIndustry_in(),wi.getProvince_in());
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			result.setCount(this.count(effectivePerson, business, wi.getKey(),wi.getYearMonth(),wi.getIndustry_in(),wi.getProvince_in()));
			return result;

		}
	}

	//获取客户数据列表
	private List<Customer> list(EffectivePerson effectivePerson, Business business, Integer adjustPage, Integer adjustPageSize, String keyString,
								String orderFieldName, String orderType,String yearMonth,String industry_in,String province_in) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);

		Order _order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);

		Predicate p = cb.isNotNull(root.get(Customer_.id));

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			if (StringUtils.isNotEmpty(key)) {

				Predicate p_key = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"), cb.like(root.get(Customer_.address), "%" + key + "%"),
						cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.industry), "%" + key + "%"),
						cb.like(root.get(Customer_.pinyin), "%" + key + "%"), cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"),
						cb.like(root.get(Customer_.remark), "%" + key + "%"), cb.like(root.get(Customer_.source), "%" + key + "%"),
						cb.like(root.get(Customer_.telephone), "%" + key + "%"), cb.like(root.get(Customer_.level), "%" + key + "%"),
						cb.like(root.get(Customer_.createuser), "%" + key + "%"));

				logger.info("ActionListPaging: like %" + key + " orderFieldName:" + orderFieldName + " orderType:" + orderType);
				//累加查询语句
				p = cb.and(p,p_key);

			}
		}

		Predicate p_null_empty = cb.isNotNull(root.get(Customer_.owneruser));
		p_null_empty = cb.and(p_null_empty, cb.notEqual(root.get(Customer_.owneruser), ""));
		p = cb.and(p, p_null_empty);

		// 加入 “年月” 去匹配
		if (StringUtils.isNotEmpty(yearMonth)) {
			String dayOfMonth = dayOfMonth(yearMonth); //获取月份里有多少天
			//System.out.println("dayOfMonth天数====1=1=1=1==="+dayOfMonth);
			String beginTime = yearMonth +"-01 00:00:00";
			String endTime = yearMonth +"-"+dayOfMonth+" 23:59:59";
			//System.out.println("beginTime=="+beginTime);
			//System.out.println("endTime=="+endTime);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = formatter.parse(beginTime);
			Date endDate = formatter.parse(endTime);

			Predicate p_yearMonth_in = cb.between(root.get(Customer_.createTime), beginDate, endDate);
			p = cb.and(p,p_yearMonth_in);
		}

		// 加入 “客户行业” 去匹配
		if (StringUtils.isNotEmpty(industry_in)) {
			Predicate p_industry_in = cb.equal(root.get(Customer_.industry), industry_in);
			p = cb.and(p,p_industry_in);
		}

		// 加入 “省份” 去匹配
		if (StringUtils.isNotEmpty(province_in)) {
			Predicate p_province_in = cb.like(root.get(Customer_.province), "%" + province_in + "%");
			p = cb.and(p,p_province_in);
		}

		if (null == _order) {
			cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		} else {
			cq.select(root).where(p).orderBy(_order);
		}

		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}
	// 获取客户列表数量
	private Long count(EffectivePerson effectivePerson, Business business, String keyString,String yearMonth,String industry_in,String province_in) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = cb.isNotNull(root.get(Customer_.id));
		// cq.select(cb.count(root));
		Predicate p_null_empty = cb.isNotNull(root.get(Customer_.owneruser));
		p_null_empty = cb.and(p_null_empty, cb.notEqual(root.get(Customer_.owneruser), ""));
		p = cb.and(p, p_null_empty);

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replace(keyString, "\u3000", " "));
			if (StringUtils.isNotEmpty(key)) {
				Predicate p_key = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"), cb.like(root.get(Customer_.address), "%" + key + "%"),
						cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.industry), "%" + key + "%"),
						cb.like(root.get(Customer_.pinyin), "%" + key + "%"), cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"),
						cb.like(root.get(Customer_.remark), "%" + key + "%"), cb.like(root.get(Customer_.source), "%" + key + "%"),
						cb.like(root.get(Customer_.telephone), "%" + key + "%"), cb.like(root.get(Customer_.level), "%" + key + "%"),
						cb.like(root.get(Customer_.createuser), "%" + key + "%"));
				//cq.select(cb.count(root)).where(p);
				p = cb.and(p,p_key);
			}
		}

		// 加入 “年月” 去匹配
		if (StringUtils.isNotEmpty(yearMonth)) {
			String dayOfMonth = dayOfMonth(yearMonth); //获取月份里有多少天

			String beginTime = yearMonth +"-01 00:00:00";
			String endTime = yearMonth +"-"+dayOfMonth+" 23:59:59";

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = formatter.parse(beginTime);
			Date endDate = formatter.parse(endTime);

			Predicate p_yearMonth_in = cb.between(root.get(Customer_.createTime), beginDate, endDate);
			p = cb.and(p,p_yearMonth_in);
		}

		// 加入 “客户行业” 去匹配
		if (StringUtils.isNotEmpty(industry_in)) {
			Predicate p_industry_in = cb.equal(root.get(Customer_.industry), industry_in);
			p = cb.and(p,p_industry_in);
		}

		// 加入 “省份” 去匹配
		if (StringUtils.isNotEmpty(province_in)) {
			Predicate p_province_in = cb.like(root.get(Customer_.province), "%" + province_in + "%");
			p = cb.and(p,p_province_in);
		}

		cq.select(cb.count(root)).where(p);

		return em.createQuery(cq).getSingleResult();
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}
	//对应前端传入的参数，设置set和get方法
	public static class Wi extends GsonPropertyObject {
		@FieldDescribe("年月 如2020-05")
		private String yearMonth;

		@FieldDescribe("客户行业")
		private String industry_in;

		@FieldDescribe("省份")
		private String province_in;

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

		public String getYearMonth() { return yearMonth; }

		public void setYearMonth(String yearMonth) { this.yearMonth = yearMonth; }

		public String getIndustry_in() {
			return industry_in;
		}

		public void setIndustry_in(String industry_in) {
			this.industry_in = industry_in;
		}


		public String getProvince_in() {
			return province_in;
		}

		public void setProvince_in(String province_in) {
			this.province_in = province_in;
		}
	}

	/**
	 *  通过年月，获取月份中有多少天
	 * @param forYM 年月，如：2020-02
	 * @return 天数
	 */
	public static String dayOfMonth(String forYM) {

		int year = Integer.parseInt(StringUtils.left(forYM, 4));
		int month = Integer.parseInt(StringUtils.right(forYM, 2));
		Calendar c = Calendar.getInstance();
		c.set(year, month, 0); //输入类型为int类型

		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

		//System.out.println(year + "年" + month + "月有" + dayOfMonth + "天");

		return String.valueOf(dayOfMonth);

	}

}
