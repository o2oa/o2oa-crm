package com.x.wcrm.assemble.control.wrapout;

import java.util.ArrayList;
import java.util.List;

import com.x.base.core.entity.JpaObject;
import com.x.wcrm.core.entity.OpportunityStatus;

public class WrapOutOpportunityStatus extends OpportunityStatus {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5602003047305496925L;

	public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

	private Long rank;

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}
}
