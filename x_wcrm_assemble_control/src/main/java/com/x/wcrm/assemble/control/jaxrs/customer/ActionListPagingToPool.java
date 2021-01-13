package com.x.wcrm.assemble.control.jaxrs.customer;

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
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.tools.DateOperation;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class ActionListPagingToPool extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ActionListPagingToPool.class);
	private final static int TO_DEALS_DATE = -15; //当前天数的前15天
	// 待进入公海客户分页查询，1，未锁定2，未跟进（15天内）3，未成交（15天内）
	ActionResult<List<Wo>> Execute_Paging_toPool(EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, JsonElement jsonElement)
			throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<Customer> os = this.list(effectivePerson, business, adjustPage, adjustPageSize, wi.getKey(), wi.getOrderFieldName(), wi.getOrderType());
			List<Wo> wos = Wo.copier.copy(os);
			result.setData(wos);
			result.setCount(this.count(effectivePerson, business, wi.getKey()));
			return result;

		}
	}

	//获取客户数据列表
	private List<Customer> list(EffectivePerson effectivePerson, Business business, Integer adjustPage, Integer adjustPageSize, String keyString,
								String orderFieldName, String orderType) throws Exception {
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

		p = cb.and(p, cb.isNotNull(root.get(Customer_.owneruser))); //owneruser 不为 ""
		p = cb.and(p, cb.notEqual(root.get(Customer_.owneruser), "")); //owneruser 不为 ""
		p = cb.and(p, cb.equal(root.get(Customer_.islock), CustomerService.UNLOCK)); //客户未锁定
		p = cb.and(p, cb.notEqual(root.get(Customer_.dealstatus), CustomerService.MAKE_A_DEAL)); //不等于成交

		DateOperation dateop = new DateOperation();
		Date today = new Date();
		String _tmpDateString1 = dateop.getDayAdd2(today, TO_DEALS_DATE);
		//String _tmpDateString2 = dateop.getDayAdd(today, -10);
		Date _tmpDate1 = dateop.getDateFromString(_tmpDateString1, "yyyy-MM-dd HH:mm:ss");
		//Date _tmpDate2 = dateop.getDateFromString(_tmpDateString2, "yyyy-MM-dd HH:mm:ss");

		Predicate p1 = cb.lessThan(root.get(Customer_.createTime), _tmpDate1); //创建时间
		Predicate p2 = cb.lessThan(root.get(Customer_.updateTime), _tmpDate1);

		Predicate date_p = cb.or(p1, p2);

		p = cb.and(p, date_p);


		if (null == _order) {
			cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		} else {
			cq.select(root).where(p).orderBy(_order);
		}

		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}
	// 获取客户列表数量
	private Long count(EffectivePerson effectivePerson, Business business, String keyString) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = cb.isNotNull(root.get(Customer_.id));
		// cq.select(cb.count(root));

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

		p = cb.and(p, cb.isNotNull(root.get(Customer_.owneruser))); //owneruser 不为 ""
		p = cb.and(p, cb.notEqual(root.get(Customer_.owneruser), "")); //owneruser 不为 ""
		p = cb.and(p, cb.equal(root.get(Customer_.islock), CustomerService.UNLOCK)); //客户未锁定
		p = cb.and(p, cb.notEqual(root.get(Customer_.dealstatus), CustomerService.MAKE_A_DEAL)); //不等于成交

		DateOperation dateop = new DateOperation();
		Date today = new Date();
		String _tmpDateString1 = dateop.getDayAdd2(today, TO_DEALS_DATE);
		//String _tmpDateString2 = dateop.getDayAdd(today, -10);
		Date _tmpDate1 = dateop.getDateFromString(_tmpDateString1, "yyyy-MM-dd HH:mm:ss");
		//Date _tmpDate2 = dateop.getDateFromString(_tmpDateString2, "yyyy-MM-dd HH:mm:ss");

		Predicate p1 = cb.lessThan(root.get(Customer_.createTime), _tmpDate1); //创建时间
		Predicate p2 = cb.lessThan(root.get(Customer_.updateTime), _tmpDate1);

		Predicate date_p = cb.or(p1, p2);

		p = cb.and(p, date_p);

		cq.select(cb.count(root)).where(p);

		return em.createQuery(cq).getSingleResult();
	}

	public static class Wo extends Customer {
		private static final long serialVersionUID = 1276641320278402941L;
		static WrapCopier<Customer, Wo> copier = WrapCopierFactory.wo(Customer.class, Wo.class, null, JpaObject.FieldsInvisible, false);
	}
	//对应前端传入的参数，设置set和get方法
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
