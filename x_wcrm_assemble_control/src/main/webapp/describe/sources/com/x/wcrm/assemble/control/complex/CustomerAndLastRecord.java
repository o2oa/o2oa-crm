package com.x.wcrm.assemble.control.complex;

import java.util.Date;

import com.x.wcrm.core.entity.Customer;

public class CustomerAndLastRecord extends Customer {

	/**
	 *
	 */
	private static final long serialVersionUID = 4652899787876186085L;
	Date lastRecord_Time;
	String lastRecord_Id; //跟进记录本身的uuid
	String crmid;	//客户，商机等uuid
	Long lastRecord_LongTime;

	public Date getLastRecord_Time() {
		return lastRecord_Time;
	}

	public void setLastRecord_Time(Date lastRecord_Time) {
		this.lastRecord_Time = lastRecord_Time;
	}

	public String getLastRecord_Id() {
		return lastRecord_Id;
	}

	public void setLastRecord_Id(String lastRecord_Id) {
		this.lastRecord_Id = lastRecord_Id;
	}

	public Long getLastRecord_LongTime() {
		return lastRecord_LongTime;
	}

	public void setLastRecord_LongTime(Long lastRecord_LongTime) {
		this.lastRecord_LongTime = lastRecord_LongTime;
	}

}
