package com.x.wcrm.assemble.control.factory;

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
import com.x.wcrm.assemble.control.jaxrs.leads.BaseAction;
import com.x.wcrm.core.entity.Customer_;
import com.x.wcrm.core.entity.Leads;
import com.x.wcrm.core.entity.Leads_;

public class LeadsFactory extends AbstractFactory {

	SingularAttribute<JpaObject, Date> defaultOrder = Leads_.createTime;

	public LeadsFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public Leads get(String LeadsId) throws Exception {
		return this.entityManagerContainer().find(LeadsId, Leads.class);
	}

	public List<String> fetchAllIds() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Leads> root = cq.from(Leads.class);
		cq.select(root.get(Leads_.id));

		return em.createQuery(cq).getResultList();
	}

	public List<String> fetchAllIdsByCreator(String distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Leads> root = cq.from(Leads.class);
		// Predicate p = cb.like(root.get(Building_.pinyin), str + "%");
		Predicate p = cb.equal(root.get(Leads_.createuser), distinguishName);
		cq.select(root.get(Leads_.id)).where(p).orderBy(cb.desc(root.get(Leads_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//创建时间倒序现在
	public List<Leads> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Leads> cq = cb.createQuery(Leads.class);
		Root<Leads> root = cq.from(Leads.class);
		cq.select(root).orderBy(cb.desc(root.get(Leads_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据责任人列表获得线索列表
	public List<Leads> ListByOwnerList(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize,
			String keyString, String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Leads> cq = cb.createQuery(Leads.class);
		Root<Leads> root = cq.from(Leads.class);

		Predicate p = root.get(Leads_.owneruser).in(_distinguishNameList);
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Leads_.name), "%" + key + "%"),
					cb.like(root.get(Leads_.cellphone), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyin), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyinInitial), "%" + key + "%"),
					cb.like(root.get(Leads_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));

		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Leads_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		//		System.out.println("LeadsFactory.ListByOwnerList():" + cq.toString());
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

	//根据责任人列表获得线索数量
	public Long ListByOwnerList_Count(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Leads> root = cq.from(Leads.class);

		Predicate p = root.get(Leads_.owneruser).in(_distinguishNameList);
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Leads_.name), "%" + key + "%"),
					cb.like(root.get(Leads_.cellphone), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyin), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyinInitial), "%" + key + "%"),
					cb.like(root.get(Leads_.telephone), "%" + key + "%"));
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

	//责任人是我或者我的下属,或者当前人包含在团队成员内（拥有读写权限，或者制度权限）
	public List<Leads> ListByOwnerList_And_TeamMembersReadAndWrite(List<String> _owner_distinguishNameList,
			String _w_r_distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Leads> cq = cb.createQuery(Leads.class);
		Root<Leads> root = cq.from(Leads.class);

		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Leads_.readerUserIds));
		cb.or(p, cb.isMember(_w_r_distinguishName, root.get(Leads_.writerUserIds)));
		cb.or(p, root.get(Leads_.owneruser).in(_owner_distinguishNameList));
		cq.select(root).where(p).orderBy(cb.desc(root.get(Leads_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据责任人列表获得线索列表 (已转化)
	public List<Leads> ListByOwnerList_HasTransform(List<String> _distinguishNameList, Integer adjustPage,
			Integer adjustPageSize, String keyString, String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Leads> cq = cb.createQuery(Leads.class);
		Root<Leads> root = cq.from(Leads.class);

		Predicate p = root.get(Leads_.owneruser).in(_distinguishNameList);
		p = cb.and(p, cb.equal(root.get(Leads_.istransform), BaseAction.HAS_TRANSFORM));
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Leads_.name), "%" + key + "%"),
					cb.like(root.get(Leads_.cellphone), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyin), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyinInitial), "%" + key + "%"),
					cb.like(root.get(Leads_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));

		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Leads_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		//		System.out.println("LeadsFactory.ListByOwnerList():" + cq.toString());
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();
	}

	//根据责任人列表获得线索数量
	public Long ListByOwnerList_HasTransform_Count(List<String> _distinguishNameList, String keyString)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Leads.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Leads> root = cq.from(Leads.class);

		Predicate p = root.get(Leads_.owneruser).in(_distinguishNameList);
		p = cb.and(p, cb.equal(root.get(Leads_.istransform), BaseAction.HAS_TRANSFORM));
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Leads_.name), "%" + key + "%"),
					cb.like(root.get(Leads_.cellphone), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyin), "%" + key + "%"),
					cb.like(root.get(Leads_.pinyinInitial), "%" + key + "%"),
					cb.like(root.get(Leads_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

}
