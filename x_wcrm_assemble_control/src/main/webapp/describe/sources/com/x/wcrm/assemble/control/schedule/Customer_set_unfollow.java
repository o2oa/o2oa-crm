package com.x.wcrm.assemble.control.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.schedule.AbstractJob;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.CustomerAndLastRecord;
import com.x.wcrm.assemble.control.jaxrs.customer.ActionLock.Wo;
import com.x.wcrm.assemble.control.staticconfig.FollowConfig;
import com.x.wcrm.core.entity.Customer;

public class Customer_set_unfollow extends AbstractJob {

	/***
	 * 
	 * 
	 * */

	private static Logger logger = LoggerFactory.getLogger(Customer_set_unfollow.class);
	private final static String MODULE_NAME = "customer";
	private final static int RECORD_DATE = 5; //当前天数的前5天

	@Override
	public void schedule(JobExecutionContext jobExecutionContext) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		logger.info("5天未跟进（没有跟进近路）的客户，设置跟进状态为：未跟进");

		//int _count = 5;
		List<CustomerAndLastRecord> resultList = new ArrayList<>();

		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			Business business = new Business(emc);
			List<CustomerAndLastRecord> elements = business.customerFactory().List_last_optionTime(MODULE_NAME);
			Date currentDate = new Date();

			for (CustomerAndLastRecord e : elements) {
				int _tmpint = (int) (currentDate.getTime() - e.getLastRecord_LongTime()) / (1000 * 3600 * 24);
				if (_tmpint >= RECORD_DATE) {
					resultList.add(e);
				}
			}

			logger.info("resultList:" + resultList.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {

			for (CustomerAndLastRecord r : resultList) {
				Customer o = emc.find(r.getLastRecord_Id(), Customer.class);

				if (null == o) {
					//					throw new ExceptionEntityNotExist(o.getId(), Customer.class);
				} else {
					o.setFollow(FollowConfig.UNFOLLOW);
					logger.info(r.getCustomername() + ":最后一期跟进日期" + r.getLastRecord_Time() + "。即将被设置为。" + FollowConfig.UNFOLLOW);

					emc.beginTransaction(Customer.class);
					emc.persist(o, CheckPersistType.all);
					emc.commit();
				}

			}

		}

	}

}
