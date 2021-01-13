package com.x.wcrm.assemble.control.factory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.OpportunityType;
import com.x.wcrm.core.entity.OpportunityType_;

public class OpportunityTypeFactory extends AbstractFactory {

	public OpportunityTypeFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public OpportunityType get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, OpportunityType.class);
	}

	
	//判断商机组id是有在数据库中存在
	public boolean IsOpportunityTypeAvailable(String id) throws Exception {
		List<String> ids = this.entityManagerContainer().ids(OpportunityType.class);
		if (ids.indexOf(id) >= 0) {
			return true;
		} else {
			return false;
		}
	}


	// 创建时间倒序现在
	public List<OpportunityType> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(OpportunityType.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OpportunityType> cq = cb.createQuery(OpportunityType.class);
		Root<OpportunityType> root = cq.from(OpportunityType.class);
		cq.select(root).orderBy(cb.desc(root.get(OpportunityType_.createTime)));
		return em.createQuery(cq).getResultList();
	}
}
