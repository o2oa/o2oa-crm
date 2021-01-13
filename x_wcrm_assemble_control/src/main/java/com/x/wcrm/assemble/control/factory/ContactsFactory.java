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

import com.x.wcrm.assemble.control.jaxrs.common.StringWCRMUtils;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;
import org.apache.commons.lang3.StringUtils;

import com.x.base.core.entity.JpaObject;
import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Contacts_;

public class ContactsFactory extends AbstractFactory {
	SingularAttribute<JpaObject, Date> defaultOrder = Contacts_.createTime;

	public ContactsFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public Contacts get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, Contacts.class);
	}

	//创建时间倒序排列
	public List<Contacts> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		cq.select(root).orderBy(cb.desc(root.get(Contacts_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据客户uuid获得联系人列表
	public List<Contacts> ListByCustomerId(String customerId) throws Exception {

		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = cb.equal(root.get(Contacts_.customerid), customerId);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Contacts_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	/*
	 * 根据客户uuid获得联系人列表，分页查询
	 * */
	public List<Contacts> ListByCustomerId_Paging(String customerId, Integer adjustPage, Integer adjustPageSize, String orderFieldName,
			String orderType) throws Exception {

		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = cb.equal(root.get(Contacts_.customerid), customerId);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Contacts_.createTime)));

		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(Contacts_.createTime));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Contacts_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	/*
	 * 根据客户uuid获得联系人列表，分页查询
	 * */
	public Long ListByCustomerId_Count(String customerId) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = cb.equal(root.get(Contacts_.customerid), customerId);
		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	public List<Contacts> ListByUUIDList(List<String> uuid) throws Exception {

		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = root.get(Contacts_.id).in(uuid);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Contacts_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据责任人列表获得联系人列表
	public List<Contacts> ListByOwnerList(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize, String keyString,
										  String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = root.get(Contacts_.owneruser).in(_distinguishNameList);

		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Contacts_.contactsname), "%" + key + "%"),
					cb.like(root.get(Contacts_.cellphone), "%" + key + "%"), cb.like(root.get(Contacts_.pinyin), "%" + key + "%"),
					cb.like(root.get(Contacts_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Contacts_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Contacts_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	//责任人是我或者我的下属,或者当前人包含在团队成员内（拥有读写权限，或者制度权限）
	public List<Contacts> ListByOwnerList_And_TeamMembersReadAndWrite(List<String> _owner_distinguishNameList, String _w_r_distinguishName)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);

		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Contacts_.readerUserIds));
		cb.or(p, cb.isMember(_w_r_distinguishName, root.get(Contacts_.writerUserIds)));
		cb.or(p, root.get(Contacts_.owneruser).in(_owner_distinguishNameList));
		cq.select(root).where(p).orderBy(cb.desc(root.get(Contacts_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据责任人列表获得客户数量
	public Long ListByOwnerList_Count(List<String> _distinguishNameList, String keyString) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Contacts> root = cq.from(Contacts.class);

		Predicate p = root.get(Contacts_.owneruser).in(_distinguishNameList);
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Contacts_.contactsname), "%" + key + "%"),
					cb.like(root.get(Contacts_.cellphone), "%" + key + "%"), cb.like(root.get(Contacts_.pinyin), "%" + key + "%"),
					cb.like(root.get(Contacts_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Contacts_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	//参与的：1，我是责任人，2包含在团队成员中（拥有只读、或者读写权限）
	public List<Contacts> List_OwnerEqual_Or_ReadersMember_Or_WritesMember(String _distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = cb.equal(root.get(Contacts_.owneruser), _distinguishName);
		cb.or(p, cb.isMember(_distinguishName, root.get(Contacts_.writerUserIds)));
		cb.or(p, cb.isMember(_distinguishName, root.get(Contacts_.readerUserIds)));
		cq.select(root).where(p).orderBy(cb.desc(root.get(Contacts_.createTime)));
		return em.createQuery(cq).getResultList();
	}

}
