package com.x.wcrm.assemble.control.schedule;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.x.base.core.project.schedule.AbstractJob;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.service.CustomerService;
import com.x.wcrm.assemble.control.tools.DateOperation;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Customer_;

/*
 * 没有按照制定时间成交的放入公海
 * */
//public class OpenSea_deals implements Job {
public class OpenSea_deals extends AbstractJob {
	private static Logger logger = LoggerFactory.getLogger(OpenSea_deals.class);
	private final static int TO_DEALS_DATE = -20; //当前天数的前20天

	/*
	 * 0.负责人不为空
	 * 1.未锁定状态.
	 * 2.未成交状态
	 * 3.测试时间：或者20天不成交.
	 * */
	//@Override
	//	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	//		// TODO Auto-generated method stub
	//		logger.info("检查符合放入公海的客户。并且自动放入公海");
	//
	//		//int _count = 5;
	//
	//		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
	//			Business business = new Business(emc);
	//			EntityManager em = emc.get(Customer.class);
	//			CriteriaBuilder cb = em.getCriteriaBuilder();
	//			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
	//			Root<Customer> root = cq.from(Customer.class);
	//
	//			Predicate p = cb.isNotNull(root.get(Customer_.owneruser)); //owneruser 不为null
	//			p = cb.and(p, cb.notEqual(root.get(Customer_.owneruser), "")); //owneruser 不为 ""
	//			p = cb.and(p, cb.notEqual(root.get(Customer_.islock), CustomerService.UNLOCK)); //客户未锁定
	//			p = cb.and(p, cb.notEqual(root.get(Customer_.dealstatus), CustomerService.MAKE_A_DEAL)); //不等于成交
	//
	//			DateOperation dateop = new DateOperation();
	//			Date today = new Date();
	//			String _tmpDateString1 = dateop.getDayAdd2(today, TO_DEALS_DATE);
	//			//String _tmpDateString2 = dateop.getDayAdd(today, -10);
	//			Date _tmpDate1 = dateop.getDateFromString(_tmpDateString1, "yyyy-MM-dd HH:mm:ss");
	//			//Date _tmpDate2 = dateop.getDateFromString(_tmpDateString2, "yyyy-MM-dd HH:mm:ss");
	//
	//			Predicate p1 = cb.lessThan(root.get(Customer_.createTime), _tmpDate1); //创建时间
	//			Predicate p2 = cb.lessThan(root.get(Customer_.updateTime), _tmpDate1);
	//
	//			Predicate date_p = cb.or(p1, p2);
	//
	//			p = cb.and(p, date_p);
	//
	//			logger.info("p1:" + p.toString());
	//
	//			cq.select(root).orderBy(cb.desc(root.get(Customer_.createTime)));
	//
	//			//			Path<String> _path_owneruser = root.get(Customer_.owneruser);
	//			//			Expression<Long> _path_count = cb.count(root);
	//			//			cq.multiselect(_path_owneruser, _path_count).where(p).groupBy(root.get(Customer_.owneruser))
	//			//					.having(cb.gt(cb.count(root.get(Customer_.owneruser)), _count));
	//
	//			List<Customer> os = em.createQuery(cq).getResultList();
	//
	//			for (Customer o : os) {
	//				logger.info(o.getCustomername() + ":" + o.getId() + "应该被放入公海");
	//			}
	//
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}

	
	/*
	 * 0.负责人不为空
	 * 1.未锁定状态.
	 * 2.未成交状态
	 * 3.测试时间：或者20天不成交.
	 * */
	@Override
	public void schedule(JobExecutionContext jobExecutionContext) throws Exception {
		// TODO Auto-generated method stub
		logger.info("检查符合放入公海的客户。并且自动放入公海");

		//int _count = 5;

		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			Business business = new Business(emc);
			EntityManager em = emc.get(Customer.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);

			Predicate p = cb.isNotNull(root.get(Customer_.owneruser)); //owneruser 不为null
			p = cb.and(p, cb.notEqual(root.get(Customer_.owneruser), "")); //owneruser 不为 ""
			p = cb.and(p, cb.equal(root.get(Customer_.islock), CustomerService.UNLOCK)); //客户未锁定
			p = cb.and(p, cb.notEqual(root.get(Customer_.dealstatus), CustomerService.MAKE_A_DEAL)); //不等于成交

			DateOperation dateop = new DateOperation();
			Date today = new Date();
			String _tmpDateString1 = dateop.getDayAdd2(today, TO_DEALS_DATE);
			//String _tmpDateString2 = dateop.getDayAdd(today, -10);
			Date _tmpDate1 = dateop.getDateFromString(_tmpDateString1, "yyyy-MM-dd HH:mm:ss");
			//Date _tmpDate2 = dateop.getDateFromString(_tmpDateString2, "yyyy-MM-dd HH:mm:ss");

			Predicate p1 = cb.lessThan(root.get(Customer_.createTime), _tmpDate1); //创建时间
			Predicate p2 = cb.lessThan(root.get(Customer_.updateTime), _tmpDate1);

			Predicate date_p = cb.or(p1, p2);

			p = cb.and(p, date_p);

			logger.info("p1:" + p.toString());

			cq.select(root).orderBy(cb.desc(root.get(Customer_.createTime)));

			//			Path<String> _path_owneruser = root.get(Customer_.owneruser);
			//			Expression<Long> _path_count = cb.count(root);
			//			cq.multiselect(_path_owneruser, _path_count).where(p).groupBy(root.get(Customer_.owneruser))
			//					.having(cb.gt(cb.count(root.get(Customer_.owneruser)), _count));

			List<Customer> os = em.createQuery(cq).getResultList();

			for (Customer o : os) {
				logger.info(o.getCustomername() + ":" + o.getId() + "应该被放入公海");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
