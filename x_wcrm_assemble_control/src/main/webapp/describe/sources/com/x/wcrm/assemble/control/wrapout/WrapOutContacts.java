package com.x.wcrm.assemble.control.wrapout;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.entity.JpaObject;
import com.x.wcrm.core.entity.Contacts;
import com.x.wcrm.core.entity.Customer;

public class WrapOutContacts extends Contacts {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1049919158673898006L;

	public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

	private Long rank;

	private boolean isPool; //联系人属于的客户是否在公海中。

	private Customer customer;

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public boolean isPool() {
		return isPool;
	}

	public void setPool(boolean isPool) {
		this.isPool = isPool;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	

}
