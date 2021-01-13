package com.x.wcrm.assemble.control.factory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Attachment;
import com.x.wcrm.core.entity.Attachment_;

public class AttachmentFactory extends AbstractFactory {

	public AttachmentFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	//根据crmId获取附件列表，并且按照创建时间倒序排列
	public List<Attachment> listWithWcrmId(String wcrmId) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Attachment.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Attachment> cq = cb.createQuery(Attachment.class);
		Root<Attachment> root = cq.from(Attachment.class);
		Predicate p = cb.equal(root.get(Attachment_.wcrm), wcrmId);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Attachment_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	/**
	 * 根据crmId获取附件列表具有预览的属性的，并且按照创建时间倒序排列
	 * ("预览展现方式：att|img|file|zip（att不预览，img缩略图预览，file文件预览（预留），zip压缩文件（预留））.")
	 */
	public List<Attachment> listWithWcrmId_PreviewType(String wcrmId, String previewType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Attachment.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Attachment> cq = cb.createQuery(Attachment.class);
		Root<Attachment> root = cq.from(Attachment.class);
		Predicate p = cb.equal(root.get(Attachment_.wcrm), wcrmId);
		Predicate p_preview = cb.equal(root.get(Attachment_.preview), previewType);
		p = cb.and(p, p_preview);

		cq.select(root).where(p).orderBy(cb.desc(root.get(Attachment_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	public Attachment get(String id) throws Exception {
		return this.entityManagerContainer().find(id, Attachment.class);
	}

	public List<Attachment> list(List<String> ids) throws Exception {
		return this.entityManagerContainer().fetch(ids, Attachment.class);
	}

	//
	//	public List<String> fetchAllIds() throws Exception {
	//		EntityManager em = this.entityManagerContainer().get(Customer.class);
	//		CriteriaBuilder cb = em.getCriteriaBuilder();
	//		CriteriaQuery<String> cq = cb.createQuery(String.class);
	//		Root<Customer> root = cq.from(Customer.class);
	//		cq.select(root.get(Customer_.id));
	//
	//		return em.createQuery(cq).getResultList();
	//	}
	//
	//	public List<String> fetchAllIdsByCreator(String distinguishName) throws Exception {
	//		EntityManager em = this.entityManagerContainer().get(Customer.class);
	//		CriteriaBuilder cb = em.getCriteriaBuilder();
	//		CriteriaQuery<String> cq = cb.createQuery(String.class);
	//		Root<Customer> root = cq.from(Customer.class);
	//		// Predicate p = cb.like(root.get(Building_.pinyin), str + "%");
	//		Predicate p = cb.equal(root.get(Customer_.createuser), distinguishName);
	//		cq.select(root.get(Customer_.id)).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
	//		return em.createQuery(cq).getResultList();
	//	}
	//
	//	//创建时间倒序现在
	//	public List<Customer> fetchAll() throws Exception {
	//		EntityManager em = this.entityManagerContainer().get(Customer.class);
	//		CriteriaBuilder cb = em.getCriteriaBuilder();
	//		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
	//		Root<Customer> root = cq.from(Customer.class);
	//		cq.select(root).orderBy(cb.desc(root.get(Customer_.createTime)));
	//		return em.createQuery(cq).getResultList();
	//	}

	public <T extends Attachment> List<T> sort(List<T> list) {
		list = list.stream().sorted(Comparator.comparing(Attachment::getName, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
		return list;
	}
}
