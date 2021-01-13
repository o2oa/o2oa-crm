package com.x.wcrm.assemble.control.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.jaxrs.common.StringWCRMUtils;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;

public class CustomerStatisticFactory extends AbstractFactory {

	private static Logger logger = LoggerFactory.getLogger(CustomerStatisticFactory.class);
	SingularAttribute<JpaObject, Date> defaultOrder = Customer_.createTime;

	public CustomerStatisticFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	// 根据责任人列表获得用户列表
	public List<Customer> ListByOwnerList(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();

	}

	// 根据责任人列表获得客户数量
	public Long ListByOwnerList_Count(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		// 时间范围
		/*System.out.println("begintime="+begintime);
		System.out.println("endtime="+endtime);*/
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	/**
	 * 根据责任人，计算每个行业的客户数量
	 * 
	 * @throws Exception
	 */
	public List<SimpleKV> count_by_industry(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		Path<String> _path_industry = root.get(Customer_.industry);
		Expression<Long> _path_count = cb.count(root);
		cq.multiselect(_path_industry, _path_count).where(p).groupBy(root.get(Customer_.industry));
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<SimpleKV> resultList = new ArrayList<>();
		for (Tuple o : os) {
			SimpleKV e = new SimpleKV();
			e.setKey(o.get(_path_industry));
			e.setValue("" + o.get(_path_count));
			resultList.add(e);
		}
		return resultList;
	}

	/**
	 * 计算每个月的客户数量
	 * 
	 * @throws Exception
	 */
	public List<SimpleKV> count_by_month(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		Expression<String> year = cb.function("year", String.class, root.get(Customer_.createTime));
		Expression<String> month = cb.function("month", String.class, root.get(Customer_.createTime));
		//		Expression<Integer> month_Int = cb.function("month", Integer.class, root.get(Customer_.createTime));
		//String _path_yearAndMonth = ""+year+"#"+month;

		//Expression<String> _path_yearAndMonth = cb.concat(year, month);
		//Path<String> _path_month = root.get(Customer_.industry);
		Expression<String> _path_month = cb.concat(year, month);

		//		List<Expression<?>> groupByList = new ArrayList<>();
		//		groupByList.add(month);
		//		groupByList.add(month_Int);

		Expression<Long> _path_count = cb.count(root);
		//cq.multiselect(_path_month, _path_count, month_Int).where(p).groupBy(_path_month).orderBy(cb.asc(month_Int));
		cq.multiselect(_path_month, _path_count).where(p).groupBy(_path_month);
		//cq.orderBy(cb.asc(month_Int));
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<SimpleKV> resultList = new ArrayList<>();
		for (Tuple o : os) {
			SimpleKV e = new SimpleKV();
			e.setKey(o.get(_path_month));
			e.setValue("" + o.get(_path_count));
			e.setCount(o.get(_path_count));
			resultList.add(e);
		}
		return resultList;
	}

	public List<Customer> List_ByOwner_nexttime(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_nexttime_between = cb.between(root.get(Customer_.nexttime), begintime, endtime);
			p = cb.and(p, p_nexttime_between);
		}

		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(Customer_.createTime));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	public Long count_ByOwner_nexttime(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_nexttime_between = cb.between(root.get(Customer_.nexttime), begintime, endtime);
			p = cb.and(p, p_nexttime_between);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

}
