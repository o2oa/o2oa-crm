package com.x.wcrm.assemble.control.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.x.wcrm.assemble.control.jaxrs.common.OperationRecordType;
import com.x.wcrm.assemble.control.tools.DateOperation;

public class MyTest1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("==================");
		//		System.out.println("11111=测试"+WCRMModuleValues.getIdIgnoreCase("Customer")+"=");
		System.out.println("2:" + OperationRecordType.CREATE.VAL());

		DateOperation dateop = new DateOperation();
		Date today = new Date();
		System.out.println(today.toString());
		String _tmpDateString1 = dateop.getDayAdd2(today, -20);
		//String _tmpDateString2 = dateop.getDayAdd(today, -10);
		try {
			Date _tmpDate1 = dateop.getDateFromString(_tmpDateString1, "yyyy-MM-dd HH:mm:ss");
			System.out.println(_tmpDate1.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("=============================");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = "2019-10-11 18:27:13";

		Date testday;
		try {
			testday = df.parse(sDate);
			System.out.println("MyTest1.main():" + testday.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}


	}

}
