package com.x.wcrm.assemble.control.factory;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.core.entity.OperationRecord;
import com.x.wcrm.core.entity.OperationRecord_;

//操作记录
public class OperationRecordFactory extends AbstractFactory {
	public OperationRecordFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public OperationRecord get(String Id) throws Exception {
		return this.entityManagerContainer().find(Id, OperationRecord.class);
	}

	//根据关联的id（客户，线索，商机的uuid）列示操作记录，按照创建时间倒序排列
	public List<OperationRecord> ListByCrmId(String _crmId) throws Exception {

		EntityManager em = this.entityManagerContainer().get(OperationRecord.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OperationRecord> cq = cb.createQuery(OperationRecord.class);
		Root<OperationRecord> root = cq.from(OperationRecord.class);
		Predicate p = cb.equal(root.get(OperationRecord_.crmid), _crmId);
		cq.select(root).where(p).orderBy(cb.desc(root.get(OperationRecord_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//SELECT op.XCRMID,MAX(op.XCREATETIME) FROM PUBLIC.WCRM_OPERATIONRECORD AS op GROUP BY op.XCRMID;
	//SELECT op.XMODULE,op.XCRMID,MAX(op.XCREATETIME) FROM PUBLIC.WCRM_OPERATIONRECORD AS op WHERE op.XMODULE='customer' GROUP BY op.XCRMID;
	public List<SimpleKV> ListCrmIdAndCreatTime(String _module) throws Exception {
		EntityManager em = this.entityManagerContainer().get(OperationRecord.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<OperationRecord> root_op = cq.from(OperationRecord.class);
		Predicate p = cb.equal(root_op.get(OperationRecord_.module), _module);

		Path<String> _path_crmid = root_op.get(OperationRecord_.crmid);
		Path<Date> _path_createTime = root_op.get(OperationRecord_.createTime);
		Expression<Long> _path_max = cb.max(root_op.get(OperationRecord_.longupdatetime));

		cq.multiselect(_path_crmid, _path_max).where(p).groupBy(_path_crmid);
		List<Tuple> os = em.createQuery(cq).getResultList();
		for (Tuple tuple : os) {
			
		}
		return null;
	}
}
