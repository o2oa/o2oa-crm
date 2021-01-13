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
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.AbstractFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.CustomerAndLastRecord;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.jaxrs.common.StringWCRMUtils;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;
import com.x.wcrm.core.entity.OperationRecord;
import com.x.wcrm.core.entity.OperationRecord_;

public class CustomerFactory extends AbstractFactory {

	private static Logger logger = LoggerFactory.getLogger(CustomerFactory.class);
	SingularAttribute<JpaObject, Date> defaultOrder = Customer_.createTime;

	public CustomerFactory(Business business) throws Exception {
		super(business);
		// TODO Auto-generated constructor stub
	}

	public Customer get(String CustomerId) throws Exception {
		return this.entityManagerContainer().find(CustomerId, Customer.class);
	}

	// 根据id 是否存在。返回 true or false
	public Boolean IsExistById(String _id) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = cb.equal(root.get(Customer_.id), _id);
		cq.select(root.get(Customer_.id)).where(p);

		List<String> _tmpList = new ArrayList<>();
		_tmpList = em.createQuery(cq).getResultList();
		int _tmpSize = _tmpList.size();
		if (_tmpSize > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<String> fetchAllIds() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Customer> root = cq.from(Customer.class);
		cq.select(root.get(Customer_.id));

		return em.createQuery(cq).getResultList();
	}

	public List<String> fetchAllIdsByCreator(String distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Customer> root = cq.from(Customer.class);
		// Predicate p = cb.like(root.get(Building_.pinyin), str + "%");
		Predicate p = cb.equal(root.get(Customer_.createuser), distinguishName);
		cq.select(root.get(Customer_.id)).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//创建时间倒序现在
	public List<Customer> fetchAll() throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		cq.select(root).orderBy(cb.desc(root.get(Customer_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//根据id判断 一个客户是否在公海中（负责人为空，在公海中）
	public boolean isPool(String id) throws Exception {

		Customer o = this.entityManagerContainer().find(id, Customer.class);
		if (StringUtils.isBlank(o.getOwneruser()) || StringUtils.isEmpty(o.getOwneruser())) {
			return true;
		} else {
			return false;
		}
	}

	//根据责任人列表获得用户列表
	public List<Customer> ListByOwnerList(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);

		Predicate p_null_empty = cb.isNotNull(root.get(Customer_.owneruser));
		p_null_empty = cb.and(p_null_empty, cb.notEqual(root.get(Customer_.owneruser), ""));
		p = cb.and(p, p_null_empty);
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
		}
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();

	}

	//根据责任人列表获得客户数量
	public Long ListByOwnerList_Count(List<String> _distinguishNameList, String keyString) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);

		Predicate p_null_empty = cb.isNotNull(root.get(Customer_.owneruser));
		p_null_empty = cb.and(p_null_empty, cb.notEqual(root.get(Customer_.owneruser), ""));
		p = cb.and(p, p_null_empty);
		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	//列出所有负责人为空，或者负责人为null的客户。
	public List<Customer> ListALLOwnerIsEmpty() throws Exception {
		List<String> matchingList = new ArrayList<>();
		matchingList.add(null);
		matchingList.add("");

		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(matchingList);
		cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//责任人是我或者我的下属,或者当前人包含在团队成员内（拥有读写权限，或者制度权限）列表
	public List<Customer> ListByOwnerList_And_TeamMembersReadAndWrite(List<String> _owner_distinguishNameList, String _w_r_distinguishName,
			Integer adjustPage, Integer adjustPageSize, String keyString, String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Customer_.readerUserIds));
		cb.or(p, cb.isMember(_w_r_distinguishName, root.get(Customer_.writerUserIds)));
		cb.or(p, root.get(Customer_.owneruser).in(_owner_distinguishNameList));

		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
		}

		//cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		//return em.createQuery(cq).getResultList();
		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	//责任人是我或者我的下属,或者当前人包含在团队成员内（拥有读写权限，或者制度权限）数量
	public long ListByOwnerList_And_TeamMembersReadAndWrite_Count(List<String> _owner_distinguishNameList, String _w_r_distinguishName,
			String keyString) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Customer_.readerUserIds));
		cb.or(p, cb.isMember(_w_r_distinguishName, root.get(Customer_.writerUserIds)));
		cb.or(p, root.get(Customer_.owneruser).in(_owner_distinguishNameList));

		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	//所有可读写
	public List<Customer> ListAllMyCanWrite(List<String> _owner_distinguishNameList, String _w_r_distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);

		Predicate p = cb.isMember(_w_r_distinguishName, root.get(Customer_.writerUserIds));
		cb.or(p, root.get(Customer_.owneruser).in(_owner_distinguishNameList));
		cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//包含在团队成员中（拥有读写权限）
	public List<Customer> ListTeamMembers_write(String _distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = cb.isMember(_distinguishName, root.get(Customer_.writerUserIds));
		cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//包含在团队成员中（拥有读权限）
	public List<Customer> ListTeamMembers_readOnly(String _distinguishName) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = cb.isMember(_distinguishName, root.get(Customer_.readerUserIds));
		cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		return em.createQuery(cq).getResultList();
	}

	//参与的：1，我是责任人，2包含在团队成员中（拥有只读、或者读写权限）
	public List<Customer> List_OwnerEqual_Or_ReadersMember_Or_WritesMember(String _distinguishName, Integer adjustPage, Integer adjustPageSize,
			String keyString, String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = cb.equal(root.get(Customer_.owneruser), _distinguishName);
		p = cb.or(p, cb.isMember(_distinguishName, root.get(Customer_.writerUserIds)));
		p = cb.or(p, cb.isMember(_distinguishName, root.get(Customer_.readerUserIds)));

		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}
		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
		}
		//		cq.select(root).where(p).orderBy(cb.desc(root.get(Customer_.createTime)));
		//		return em.createQuery(cq).getResultList();
		cq.select(root).where(p).orderBy(_order);
		System.out.println("List_OwnerEqual_Or_ReadersMember_Or_WritesMember==="+cq.toString());
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	//参与的：1，我是责任人，2包含在团队成员中（拥有只读、或者读写权限）数量
	public long List_OwnerEqual_Or_ReadersMember_Or_WritesMember_Count(String _distinguishName, String keyString) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = cb.equal(root.get(Customer_.owneruser), _distinguishName);
		p = cb.or(p, cb.isMember(_distinguishName, root.get(Customer_.writerUserIds)));
		p = cb.or(p, cb.isMember(_distinguishName, root.get(Customer_.readerUserIds)));

		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	//列出数量大于或者等于_count
	public List<String> List_gt_count(int _count) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Customer> root = cq.from(Customer.class);
		//Predicate p = cb.isNotNull(root.get(Customer_.owneruser));
		Predicate p = cb.isNotNull(root.get(Customer_.owneruser));
		logger.info("p1:" + p.toString());
		//p = cb.and(p, cb.isNotEmpty(root.get(Customer.owneruser_FIELDNAME)));
		//logger.info("p2:" + p.toString());
		cq.multiselect(root.get(Customer_.owneruser)).where(p).groupBy(root.get(Customer_.owneruser))
				.having(cb.gt(cb.count(root.get(Customer_.owneruser)), _count));
		logger.info("cq:" + cq.toString());
		return em.createQuery(cq).getResultList();
	}

	//拥有客户的数量
	public List<SimpleKV> List_gt_count2(int _count) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Customer> root = cq.from(Customer.class);
		//Predicate p = cb.isNotNull(root.get(Customer_.owneruser));
		Predicate p = cb.isNotNull(root.get(Customer_.owneruser));
		logger.info("p1:" + p.toString());
		//p = cb.and(p, cb.isNotEmpty(root.get(Customer.owneruser_FIELDNAME)));
		//cq.multiselect(root.get(Customer_.owneruser)).where(p).groupBy(root.get(Customer_.owneruser)).having(cb.gt(cb.count(root.get(Customer_.owneruser)), _count));
		Path<String> _path_owneruser = root.get(Customer_.owneruser);
		Expression<Long> _path_count = cb.count(root);
		//cq.multiselect(root.get(Customer_.owneruser), cb.count(root)).where(p).groupBy(root.get(Customer_.owneruser)).having(cb.gt(cb.count(root.get(Customer_.owneruser)), _count));
		cq.multiselect(_path_owneruser, _path_count).where(p).groupBy(root.get(Customer_.owneruser))
				.having(cb.gt(cb.count(root.get(Customer_.owneruser)), _count));
		//		logger.info("cq:" + cq.toString());
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<SimpleKV> resultList = new ArrayList<>();
		for (Tuple o : os) {
			//			System.out.println(o.getElements());
			//			System.out.println(o.get(_path_owneruser) + ":" + o.get(_path_count));
			SimpleKV e = new SimpleKV();
			e.setKey(o.get(_path_owneruser));
			e.setValue("" + o.get(_path_count));
			resultList.add(e);
		}
		return resultList;
	}

	//锁定客户的数量
	public List<SimpleKV> List_gt_count_locked(int _locked_count) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = cb.isNotNull(root.get(Customer_.owneruser));
		p = cb.and(p, cb.equal(root.get(Customer_.islock), "1"));
		//p = cb.and(p, cb.isNotEmpty(root.get(Customer.owneruser_FIELDNAME)));
		Path<String> _path_owneruser = root.get(Customer_.owneruser);
		Expression<Long> _path_count = cb.count(root);
		cq.multiselect(_path_owneruser, _path_count).where(p).groupBy(root.get(Customer_.owneruser))
				.having(cb.gt(cb.count(root.get(Customer_.owneruser)), _locked_count));
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<SimpleKV> resultList = new ArrayList<>();
		for (Tuple o : os) {
			SimpleKV e = new SimpleKV();
			e.setKey(o.get(_path_owneruser));
			e.setValue("" + o.get(_path_count));
			resultList.add(e);
		}
		return resultList;
	}

	//SELECT op.XCRMID, max(op.XCREATETIME),c.XCUSTOMERNAME FROM PUBLIC.WCRM_OPERATIONRECORD AS op, PUBLIC.WCRM_CUSTOMER AS c WHERE c.XID = '{CustomerUUID}' and op.XCRMID IN ( SELECT c1.XID  FROM PUBLIC.WCRM_CUSTOMER AS c1 WHERE c1.XID = op.XCRMID ) GROUP BY op.XCRMID;
	//SELECT op.XCRMID,MAX(op.XLONGCREATETIME) FROM PUBLIC.WCRM_OPERATIONRECORD AS op WHERE op.XMODULE='customer' GROUP BY op.XCRMID;
	/**
	 * 列出，指定module类型的操作记录，按照crmid分组展示每组的最大值
	 * 
	 * @param {_module}
	 * @return SimpleKV
	 */
	public List<CustomerAndLastRecord> List_last_optionTime(String _module) throws Exception {
		EntityManager em = this.entityManagerContainer().get(OperationRecord.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		//Root<Customer> root_c = cq.from(Customer.class);
		Root<OperationRecord> root_op = cq.from(OperationRecord.class);

		Predicate p = cb.equal(root_op.get(OperationRecord_.module), _module);

		//Path<String> _path_customer_name = root_c.get(Customer_.id);
		Path<String> _path_oprecord_crmid = root_op.get(OperationRecord_.crmid);
		Expression<Long> _path_oprecord_createtime = cb.max(root_op.get(OperationRecord_.longcreatetime));
		//Path<Date> _path_oprecord_createtime_date =  root_op.get(OperationRecord_.createTime);

		cq.multiselect(_path_oprecord_crmid, _path_oprecord_createtime).where(p).groupBy(_path_oprecord_crmid);
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<CustomerAndLastRecord> resultList = new ArrayList<>();
		for (Tuple o : os) {
			CustomerAndLastRecord e = new CustomerAndLastRecord();
			e.setLastRecord_Id(o.get(_path_oprecord_crmid));
			e.setLastRecord_LongTime(o.get(_path_oprecord_createtime));
			
			Date date = new Date();
			date.setTime(o.get(_path_oprecord_createtime));
			//o.get(_path_oprecord_createtime);
			e.setLastRecord_Time(date);

			resultList.add(e);
		}

		return resultList;
	}

	//统计相关的方法

	/**
	 * @return {long}
	 * @param {List<String> _distinguishNameList}
	 * @throws Exception
	 * @param{Date beginTime}
	 * @param{Date endTime} 根据创建时间计算 根据责任人列表， 某一时间段内客户数据量
	 */
	public Long customer_Count_By_CreateTimeRang(List<String> _distinguishNameList, Date beginTime, Date endTime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);
		Predicate p_between = cb.between(root.get(Customer_.createTime), beginTime, endTime);

		p = cb.and(p, p_between);

		cq.select(cb.count(root)).where(p);
		return em.createQuery(cq).getSingleResult();
	}

	/**
	 * 根据计算每个省份的客户数量
	 * 
	 * @throws Exception
	 */
	public List<SimpleKV> count_by_province(List<String> _distinguishNameList,Date begintime, Date endtime) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);
		if (null != begintime && null != endtime) {
			Predicate p_createtime_between = cb.between(root.get(Customer_.createTime), begintime, endtime);
			p = cb.and(p, p_createtime_between);
		}

		Path<String> _path_province = root.get(Customer_.province);
		Expression<Long> _path_count = cb.count(root);
		cq.multiselect(_path_province, _path_count).where(p).groupBy(root.get(Customer_.province));
		List<Tuple> os = em.createQuery(cq).getResultList();
		List<SimpleKV> resultList = new ArrayList<>();
		for (Tuple o : os) {
			SimpleKV e = new SimpleKV();
			e.setKey(o.get(_path_province));
			e.setValue("" + o.get(_path_count));
			resultList.add(e);
		}
		return resultList;
	}

	/**
	 * 我负责的客户，时间范围，未跟进
	 *
	 */
	public List<Customer> List_MyDuty_NextTime_FollowStatus(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize,
			String keyString, Date beginTime, Date endTime, boolean isflow, String orderFieldName, String orderType) throws Exception {
		EntityManager em = this.entityManagerContainer().get(Customer.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);

		//跟进状态
		if (isflow) {
			Predicate p_follow_followed = cb.equal(root.get(Customer_.follow), "已跟进");
			p = cb.and(p, p_follow_followed);
		} else {
			Predicate p_follow_isnull = cb.isNull(root.get(Customer_.follow));
			Predicate p_follow_unfollow = cb.equal(root.get(Customer_.follow), "未跟进");
			Predicate p_follow = cb.or(p_follow_isnull, p_follow_unfollow);
			p = cb.and(p, p_follow);
		}

		//时间范围
		Predicate p_between = cb.between(root.get(Customer_.nexttime), beginTime, endTime);
		p = cb.and(p, p_between);

		//搜索关键字判断
		if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
			Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
					cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
					cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
			p = cb.and(p, p_like);
		}

		//排序值字段，升降序判断
		Order _order;
		if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
			_order = cb.desc(root.get(defaultOrder));
		} else {
			_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
		}

		cq.select(root).where(p).orderBy(_order);
		return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
	}

	/*	public Long Count_MyDuty_NextTime_FollowStatus(List<String> _distinguishNameList, Integer adjustPage, Integer adjustPageSize, String keyString,
				Date beginTime, Date endTime, boolean isflow, String orderFieldName, String orderType) throws Exception {
			EntityManager em = this.entityManagerContainer().get(Customer.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			Predicate p = root.get(Customer_.owneruser).in(_distinguishNameList);
			
			//跟进状态
			if (isflow) {
				Predicate p_follow_followed = cb.equal(root.get(Customer_.follow), "已跟进");
				p = cb.and(p, p_follow_followed);
			} else {
				Predicate p_follow_isnull = cb.isNull(root.get(Customer_.follow));
				Predicate p_follow_unfollow = cb.equal(root.get(Customer_.follow), "未跟进");
				Predicate p_follow = cb.or(p_follow_isnull, p_follow_unfollow);
				p = cb.and(p, p_follow);
			}
	
			//时间范围
			Predicate p_between = cb.between(root.get(Customer_.nexttime), beginTime, endTime);
			p = cb.and(p, p_between);
	
			//搜索关键字判断
			if (!StringWCRMUtils.isEmptyKeyString(keyString)) {
				String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" }, new String[] { " ", "", "" }));
				Predicate p_like = cb.or(cb.like(root.get(Customer_.customername), "%" + key + "%"),
						cb.like(root.get(Customer_.cellphone), "%" + key + "%"), cb.like(root.get(Customer_.pinyin), "%" + key + "%"),
						cb.like(root.get(Customer_.pinyinInitial), "%" + key + "%"), cb.like(root.get(Customer_.telephone), "%" + key + "%"));
				p = cb.and(p, p_like);
			}
	
			//排序值字段，升降序判断
			Order _order;
			if (StringUtils.isEmpty(orderType) || StringUtils.isEmpty(orderFieldName) || null == orderType || null == orderFieldName) {
				_order = cb.desc(root.get(defaultOrder));
			} else {
				_order = CriteriaQueryTools.setOrder(cb, root, Customer_.class, orderFieldName, orderType);
			}
	
			cq.select(root).where(p).orderBy(_order);
			return em.createQuery(cq).setFirstResult((adjustPage - 1) * adjustPageSize).setMaxResults(adjustPageSize).getResultList();
		}*/

}
