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
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Contacts_;

public class ContactsStatisticFactory extends AbstractFactory {

	SingularAttribute<JpaObject, Date> defaultOrder = Contacts_.createTime;

	public ContactsStatisticFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	// 根据责任人列表获得联系人列表
	public List<Contacts> ListByOwnerList(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize,
			String keyString, String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Contacts> cq = cb.createQuery(Contacts.class);
		Root<Contacts> root = cq.from(Contacts.class);
		Predicate p = root.get(Contacts_.owneruser).in(_distinguishNameList);

		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Contacts_.contactsname), "%" + key + "%"),
					cb.like(root.get(Contacts_.cellphone), "%" + key + "%"),
					cb.like(root.get(Contacts_.pinyin), "%" + key + "%"),
					cb.like(root.get(Contacts_.pinyinInitial), "%" + key + "%"),
					cb.like(root.get(Contacts_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Contacts_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		// 排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType
				|| null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Contacts_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize)
				.getResultList();

	}

	// 根据责任人列表获得联系人数量
	public Long ListByOwnerList_Count(List<String> _distinguishNameList, String keyString, Date begintime, Date endtime)
			throws Exception {
		EntityManager em = this.entityManagerContainer().get(Contacts.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Contacts> root = cq.from(Contacts.class);

		Predicate p = root.get(Contacts_.owneruser).in(_distinguishNameList);
		// 搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Contacts_.contactsname), "%" + key + "%"),
					cb.like(root.get(Contacts_.cellphone), "%" + key + "%"),
					cb.like(root.get(Contacts_.pinyin), "%" + key + "%"),
					cb.like(root.get(Contacts_.pinyinInitial), "%" + key + "%"),
					cb.like(root.get(Contacts_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		// 时间范围
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Contacts_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

}
