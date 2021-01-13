package com.x.wcrm.assemble.control.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.complex.CustomerAndLastRecord;

/*
 * 
 * */
public class OpenSea_Record implements Job {
	private static Logger logger = LoggerFactory.getLogger(OpenSea_Record.class);
	private final static String MODULE_NAME = "customer";
	private final static int TO_RECORD_DATE = 10; //10天未跟进

	/*
	 * 0.负责人不为空
	 * 1.未锁定状态.
	 * 2.未成交状态
	 * 3.测试时间：10天没有跟进记录.
	 * */

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		// TODO Auto-generated method stub

		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			Business business = new Business(emc);
			List<CustomerAndLastRecord> elements = business.customerFactory().List_last_optionTime(MODULE_NAME);
			Date currentDate = new Date();

			List<CustomerAndLastRecord> resultList = new ArrayList<>();

			for (CustomerAndLastRecord e : elements) {
				int _tmpint = (int) (currentDate.getTime() - e.getLastRecord_LongTime()) / (1000 * 3600 * 24);
				if (_tmpint >= TO_RECORD_DATE) {
					resultList.add(e); 
				}
			}

			for (CustomerAndLastRecord o : resultList) {
				logger.info(o.getCustomername()+":最后一期跟进日期"+o.getLastRecord_Time()+"。即将被放入公海。");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
