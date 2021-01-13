package com.x.wcrm.assemble.control.factory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityStatus_;

public class OpportunityStatusFactory extends AbstractFactory {

	public OpportunityStatusFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public OpportunityStatus get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, OpportunityStatus.class);
	}

	// 判断同一商机状态组下的排序号是否重复
	public boolean IsAvailable_OpportunityStatus_OrderId_OpportunityType(String typeid, Integer orderid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(OpportunityStatus.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<OpportunityStatus> root = cq.from(OpportunityStatus.class);
		Predicate p = cb.equal(root.get(OpportunityStatus_.typeid), typeid);
		cq.select(root.get(OpportunityStatus_.orderid)).where(p);
		List<Integer> orderidList = em.createQuery(cq).getResultList();
		if (orderidList.indexOf(orderid) >= 0) {
			//已被使用返回false
			return false;
		} else {
			//未被使用返回true
			return true;
		}
	}

	// 列出所有 创建时间倒序现在
	public List<OpportunityStatus> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(OpportunityStatus.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OpportunityStatus> cq = cb.createQuery(OpportunityStatus.class);
		Root<OpportunityStatus> root = cq.from(OpportunityStatus.class);
		cq.select(root).orderBy(cb.desc(root.get(OpportunityStatus_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	// 根据TypeId列出所有关联的商机状态，倒序排列
	public List<OpportunityStatus> ListByTypeId(String typeid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(OpportunityStatus.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OpportunityStatus> cq = cb.createQuery(OpportunityStatus.class);
		Root<OpportunityStatus> root = cq.from(OpportunityStatus.class);
		Predicate p = cb.equal(root.get(OpportunityStatus_.typeid), typeid);
		cq.select(root).where(p).orderBy(cb.desc(root.get(OpportunityStatus_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	public List<OpportunityStatus> ListByTypeIdOrderByOrderId_Desc(String typeid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(OpportunityStatus.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OpportunityStatus> cq = cb.createQuery(OpportunityStatus.class);
		Root<OpportunityStatus> root = cq.from(OpportunityStatus.class);
		Predicate p = cb.equal(root.get(OpportunityStatus_.typeid), typeid);
		cq.select(root).where(p).orderBy(cb.desc(root.get(OpportunityStatus_.orderid)));
		return em.createQuery(cq).getResultList();
	}

	public List<OpportunityStatus> ListByTypeIdOrderByOrderId_Asc(String typeid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(OpportunityStatus.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OpportunityStatus> cq = cb.createQuery(OpportunityStatus.class);
		Root<OpportunityStatus> root = cq.from(OpportunityStatus.class);
		Predicate p = cb.equal(root.get(OpportunityStatus_.typeid), typeid);
		cq.select(root).where(p).orderBy(cb.asc(root.get(OpportunityStatus_.orderid)));
		return em.createQuery(cq).getResultList();
	}

}
