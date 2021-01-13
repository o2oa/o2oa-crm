package com.x.wcrm.assemble.control.factory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.ContactsAndOpportunity;
import com.x.wcrm.core.entity.ContactsAndOpportunity_;

public class ContactsAndOpportunityFactory extends AbstractFactory {
	public ContactsAndOpportunityFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public ContactsAndOpportunity get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, ContactsAndOpportunity.class);
	}

	//创建时间倒序排列
	public List<ContactsAndOpportunity> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(ContactsAndOpportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContactsAndOpportunity> cq = cb.createQuery(ContactsAndOpportunity.class);
		Root<ContactsAndOpportunity> root = cq.from(ContactsAndOpportunity.class);
		cq.select(root).orderBy(cb.desc(root.get(ContactsAndOpportunity_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据商机id列示联系人列表
	//	public List<ContactsAndOpportunity> ListContactsByOpportunityId(String opportunityid) throws Exception {
	//		EntityManager em = this.entityManagerContainer().get(ContactsAndOpportunity.class);
	//		CriteriaBuilder cb = em.getCriteriaBuilder();
	//		CriteriaQuery<ContactsAndOpportunity> cq = cb.createQuery(ContactsAndOpportunity.class);
	//		Root<ContactsAndOpportunity> root = cq.from(ContactsAndOpportunity.class);
	//		Predicate p = cb.equal(root.get(ContactsAndOpportunity_.opportunityid), opportunityid);
	//		cq.select(root).where(p).orderBy(cb.desc(root.get(ContactsAndOpportunity_.createTime)));
	//		return em.createQuery(cq).getResultList();
	//	}

	//根据商机id,列示联系人id列表
	public List<String> ListContactsIds_ByOpportunityId(String opportunityid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(ContactsAndOpportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<ContactsAndOpportunity> root = cq.from(ContactsAndOpportunity.class);
		Predicate p = cb.equal(root.get(ContactsAndOpportunity_.opportunityid), opportunityid);
		cq.select(root.get(ContactsAndOpportunity_.contactsid)).where(p).orderBy(cb.desc(root.get(ContactsAndOpportunity_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据商机id,列示关联的对象
	public List<ContactsAndOpportunity> getOne_By_OpportunityId_And_ContactsId(String opportunityid, String contactsid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(ContactsAndOpportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContactsAndOpportunity> cq = cb.createQuery(ContactsAndOpportunity.class);
		Root<ContactsAndOpportunity> root = cq.from(ContactsAndOpportunity.class);
		Predicate p = cb.equal(root.get(ContactsAndOpportunity_.opportunityid), opportunityid);//商机id
		p = cb.and(p, cb.equal(root.get(ContactsAndOpportunity_.contactsid), contactsid));//联系人id
		cq.select(root).where(p).orderBy(cb.desc(root.get(ContactsAndOpportunity_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	public List<String> ListOpportunityIds_ByContactsId(String contactsid) throws Exception {
		EntityManager em = this.entityManagerContainer().get(ContactsAndOpportunity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<ContactsAndOpportunity> root = cq.from(ContactsAndOpportunity.class);
		Predicate p = cb.equal(root.get(ContactsAndOpportunity_.contactsid), contactsid);
		cq.select(root.get(ContactsAndOpportunity_.opportunityid)).where(p).orderBy(cb.desc(root.get(ContactsAndOpportunity_.createTime)));
		return em.createQuery(cq).getResultList();
	}
	

}
