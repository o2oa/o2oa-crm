package com.x.wcrm.core.entity.tools;

import java.util.Date;

public class EntityDateTools {

	/*
	 * 根据时间返回 数字格式结果
	 * */
	public Long Date2Long(Date date) {
//		Long result_long = 0L;
//		result_long = date.getTime();
		return date.getTime();
	}

//	public static void main(String[] args) {
//		EntityDateTools _this = new EntityDateTools();
//		String string_date = "3019-10-11 18:27:13";
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:dd:ss");
//		Date d;
//		try {
//			d = f.parse(string_date);
//			Long res_long = _this.Date2Long(d);
//			System.out.println(res_long);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			 e.printStackTrace();
//		}
//
//	}

}
