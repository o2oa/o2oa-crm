package com.x.wcrm.assemble.control.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.entity.JpaObject;
import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.jaxrs.common.StringWCRMUtils;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.Opportunity_;

public class OpportunityFactory extends AbstractFactory {

	SingularAttribute<JpaObject, Date> defaultOrder = Opportunity_.createTime;

	public OpportunityFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public Opportunity get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, Opportunity.class);
	}

	// 根据id 是否存在。返回 true or false
	public Boolean IsExistById(String _id) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = cb.equal(root.get(Opportunity_.id), _id);
		cq.select(root.get(Opportunity_.id)).where(p);

		List<String> _tmpList = new ArrayList<>();
		_tmpList = em.createQuery(cq).getResultList();
		int _tmpSize = _tmpList.size();
		if (_tmpSize > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<Opportunity> ListByCustomerId(String customerId) throws Exception {

		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Opportunity> cq = cb.createQuery(Opportunity.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = cb.equal(root.get(Opportunity_.customerid), customerId);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Opportunity_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	// 根据责任人列表获得用户列表
	public List<Opportunity> ListByOwnerList(List<String> _distinguishNameList, Integer adjustPage,
			Integer adjustPageSize, String keyString, String orderFieldName, String orderType, Date begintime,
			Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Opportunity> cq = cb.createQuery(Opportunity.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = root.get(Opportunity_.owneruser).in(_distinguishNameList);
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Opportunity_.opportunityname), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyin), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyinInitial), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Opportunity_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

	// 根据责任人列表获得用户列表,数量
	public long ListByOwnerList_Count(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = root.get(Opportunity_.owneruser).in(_distinguishNameList);
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Opportunity_.opportunityname), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyin), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyinInitial), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	// 责任人是我或者我的下属,或者当前人包含在团队成员内（拥有读写权限，或者只读权限）
	public List<Opportunity> ListByOwnerList_And_TeamMembersReadAndWrite(List<String> _owner_distinguishNameList,
			String _w_r_distinguishName, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Opportunity> cq = cb.createQuery(Opportunity.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Opportunity_.readerUserIds));
		cb.or(p, cb.isMember(_w_r_distinguishName, root.get(Opportunity_.writerUserIds)));
		cb.or(p, root.get(Opportunity_.owneruser).in(_owner_distinguishNameList));
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Opportunity_.opportunityname), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyin), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyinInitial), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Opportunity_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

	// 责任人是我或者我的下属,或者当前人包含在团队成员内（拥有读写权限，或者只读权限），数量
	public long ListByOwnerList_And_TeamMembersReadAndWrite_Count(List<String> _owner_distinguishNameList,
			String _w_r_distinguishName, String keyString) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Opportunity_.readerUserIds));
		cb.or(p, cb.isMember(_w_r_distinguishName, root.get(Opportunity_.writerUserIds)));
		cb.or(p, root.get(Opportunity_.owneruser).in(_owner_distinguishNameList));
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Opportunity_.opportunityname), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyin), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyinInitial), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	// 参与的：1，我是责任人，2包含在团队成员中（拥有只读、或者读写权限）列表
	public List<Opportunity> List_OwnerEqual_Or_ReadersMember_Or_WritesMember(String _distinguishName,
			Integer adjustPage, Integer adjustPageSize, String keyString, String orderFieldName, String orderType)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Opportunity> cq = cb.createQuery(Opportunity.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = cb.equal(root.get(Opportunity_.owneruser), _distinguishName);
		cb.or(p, cb.isMember(_distinguishName, root.get(Opportunity_.writerUserIds)));
		cb.or(p, cb.isMember(_distinguishName, root.get(Opportunity_.readerUserIds)));
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Opportunity_.opportunityname), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyin), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyinInitial), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Opportunity_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

	// 参与的：1，我是责任人，2包含在团队成员中（拥有只读、或者读写权限）数量
	public long List_OwnerEqual_Or_ReadersMember_Or_WritesMember_Count(String _distinguishName, String keyString)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = cb.equal(root.get(Opportunity_.owneruser), _distinguishName);
		cb.or(p, cb.isMember(_distinguishName, root.get(Opportunity_.writerUserIds)));
		cb.or(p, cb.isMember(_distinguishName, root.get(Opportunity_.readerUserIds)));
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Opportunity_.opportunityname), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyin), "%" + key + "%"),
					cb.like(root.get(Opportunity_.pinyinInitial), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	public List<Opportunity> ListByUUIDList(List<String> uuid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Opportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Opportunity> cq = cb.createQuery(Opportunity.class);
		Root<Opportunity> root = cq.from(Opportunity.class);
		Predicate p = root.get(Opportunity_.id).in(uuid);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Opportunity_.createTime)));
		return em.createQuery(cq).getResultList();
	}

}
