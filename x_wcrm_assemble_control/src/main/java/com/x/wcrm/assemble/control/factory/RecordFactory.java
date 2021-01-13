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
import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.jaxrs.common.StringWCRMUtils;
import com.x.wcrm.core.entity.Record;
import com.x.wcrm.core.entity.Record_;

public class RecordFactory extends AbstractFactory {
	SingularAttribute<JpaObject, Date> defaultOrder = Record_.createTime;

	public RecordFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public Record get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, Record.class);
	}

	public List<String> fetchAllIds() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Record> root = cq.from(Record.class);
		cq.select(root.get(Record_.id));

		return em.createQuery(cq).getResultList();
	}

	public List<String> fetchAllIdsByCreator(String distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Record> root = cq.from(Record.class);
		// Predicate p = cb.like(root.get(Building_.pinyin), str + "%");
		Predicate p = cb.equal(root.get(Record_.createuser), distinguishName);
		cq.select(root.get(Record_.id)).where(p).orderBy(cb.desc(root.get(Record_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	// 创建时间倒序现在
	public List<Record> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Record> cq = cb.createQuery(Record.class);
		Root<Record> root = cq.from(Record.class);
		cq.select(root).orderBy(cb.desc(root.get(Record_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	// 根据关联的的crmid列出所有跟进记录，
	public List<Record> ListByCrmId(String crmId) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Record> cq = cb.createQuery(Record.class);
		Root<Record> root = cq.from(Record.class);
		Predicate p = cb.equal(root.get(Record_.typesid), crmId);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Record_.createTime)));
		return em.createQuery(cq).getResultList();

	}

	/**
	 * 根据模块类型,时间范围列出所有跟进记录，时间倒序排列
	 */
	public List<Record> ListByTypesAndTimeRange(String types, String orderFieldName, String orderType, Date begintime,
			Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Record> cq = cb.createQuery(Record.class);
		Root<Record> root = cq.from(Record.class);
		Predicate p = cb.isNotNull(root.get(Record_.types));
		p = cb.and(p, cb.equal(root.get(Record_.types), types));

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Record_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Record_.class, orderFieldName, orderType);
		}

		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).getResultList();

	}

	/**
	 * 根据人员列表（in）列出跟进记录列表.
	 * 
	 */
	public List<Record> List_ByCreatorNameList(List<String> _distinguishNameList, Integer adjustPage,
			Integer adjustPageSize, String keyString, String orderFieldName, String orderType, Date begintime,
			Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Record> cq = cb.createQuery(Record.class);
		Root<Record> root = cq.from(Record.class);
		Predicate p = root.get(Record_.createuser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Record_.createuser), "%" + key + "%"),
					cb.like(root.get(Record_.content), "%" + key + "%"),
					cb.like(root.get(Record_.category), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Record_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Record_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

	/**
	 * 根据人员列表（in）列出跟进记录列表.
	 * 
	 */
	public Long Count_ByCreatorNameList(List<String> _distinguishNameList, String keyString, Date begintime,
			Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Record> root = cq.from(Record.class);
		Predicate p = root.get(Record_.createuser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Record_.createuser), "%" + key + "%"),
					cb.like(root.get(Record_.content), "%" + key + "%"),
					cb.like(root.get(Record_.category), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Record_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	/**
	 * 计算每个模块的跟进数量
	 * 
	 * @throws Exception
	 */
	public List<SimpleKV> Count_GroupByTypes(List<String> _distinguishNameList, String keyString,Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Record> root = cq.from(Record.class);
		//Predicate p = cb.isNotNull(root.get(Record_.types));
		Predicate p = root.get(Record_.createuser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Record_.createuser), "%" + key + "%"),
					cb.like(root.get(Record_.content), "%" + key + "%"),
					cb.like(root.get(Record_.category), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		
		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Record_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		Path<String> _path_types = root.get(Record_.types);
		Expression<Long> _path_count = cb.count(root);
		cq.multiselect(_path_types, _path_count).where(p).groupBy(root.get(Record_.types));
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<SimpleKV> resultList = new ArrayList<>();
		for (Tuple o : os) {
			SimpleKV e = new SimpleKV();
			e.setKey(o.get(_path_types));
			//e.setValue("" + o.get(_path_count));
			e.setCount(o.get(_path_count));
			resultList.add(e);
		}
		return resultList;
	}

	public List<Record> listByTypes(String types, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Record.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Record> cq = cb.createQuery(Record.class);
		Root<Record> root = cq.from(Record.class);
		Predicate p = cb.isNotNull(root.get(Record_.types));
		p = cb.and(p, cb.equal(root.get(Record_.types), types));

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Record_.createuser), "%" + key + "%"),
					cb.like(root.get(Record_.content), "%" + key + "%"),
					cb.like(root.get(Record_.category), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Record_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}
		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Record_.class, orderFieldName, orderType);
		}

		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

}
