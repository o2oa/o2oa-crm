package com.x.wcrm.assemble.control;

import com.x.base.core.project.Context;
import com.x.base.core.project.config.Config;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.message.MessageConnector;
import com.x.wcrm.assemble.control.schedule.Customer_set_unfollow;
import com.x.wcrm.assemble.control.schedule.OpenSea_deals;

public class ThisApplication {
	protected static Context context;
	//public static Context context;

	public static final String ROLE_CRMManager = "CRMManager@CRMManagerSystemRole@R";

	public static Context context() {
		return context;
	}

	public static void init() {
		//		try {
		//			queueLoginRecord = new DocumentExistsCheckTask();
		//			context.startQueue( queueLoginRecord );
		//			context.schedule( InfoExistsCheckTask.class, "0 0/10 * * * ?");
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}

		try {
			//每十分钟执行一次公海客户检查
			//context.schedule(OpenSea.class,"* 0/10 * * * ? ");
			//context.schedule(OpenSea.class,"* 0/10 * * * ? ");
			//context.schedule(OpenSea_deals.class,"* 0/5 * * * ? ");
			//context.schedule(OpenSea_deals.class, "* 0/5 * * * ? ");
			context.schedule(OpenSea_deals.class, "* 0/15 * * * ? ");
			context.schedule(Customer_set_unfollow.class, "* 0/15 * * * ? ");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			LoggerFactory.setLevel(Config.logLevel().x_meeting_assemble_control());
			MessageConnector.start(context());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void destroy() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
