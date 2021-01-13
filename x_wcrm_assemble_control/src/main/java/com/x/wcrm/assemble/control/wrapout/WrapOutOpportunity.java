package com.x.wcrm.assemble.control.wrapout;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.entity.JpaObject;
import com.x.wcrm.core.entity.Customer;
import com.x.wcrm.core.entity.Opportunity;
import com.x.wcrm.core.entity.OpportunityStatus;
import com.x.wcrm.core.entity.OpportunityType;

public class WrapOutOpportunity extends Opportunity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1049919158673898006L;

	public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

	private Long rank;

	public OpportunityStatus opportunityStatus;

	public OpportunityType opportunityType;

	public Customer customer;

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public OpportunityStatus getOpportunityStatus() {
		return opportunityStatus;
	}

	public void setOpportunityStatus(OpportunityStatus opportunityStatus) {
		this.opportunityStatus = opportunityStatus;
	}

	public OpportunityType getOpportunityType() {
		return opportunityType;
	}

	public void setOpportunityType(OpportunityType opportunityType) {
		this.opportunityType = opportunityType;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
