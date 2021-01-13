package com.x.wcrm.assemble.control.wrapout;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.organization.Person;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Record;

public class WrapOutOperationRecord extends Record {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6824706843566959900L;

	public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

	private Long rank;

	private String DateCN; //增加一个日期格式 ：2019-10-17 星期四

	private Person person;

	private String ICONBase64;

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public String getDateCN() {
		return DateCN;
	}

	public void setDateCN(String dateCN) {
		DateCN = dateCN;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getICONBase64() {
		return ICONBase64;
	}

	public void setICONBase64(String iCONBase64) {
		ICONBase64 = iCONBase64;
	}

	public static List<? super WrapOutOperationRecord> setICONBase64_byCreateuser(Business business,
			List<? extends WrapOutOperationRecord> wos) {

		List<? super WrapOutOperationRecord> result = new ArrayList<WrapOutOperationRecord>();
		if (null != wos) {
			wos.stream().forEach(t -> {
				try {
					com.x.organization.core.entity.Person entityPerson = business.crmPersonFactory()
							.pick(t.getCreateuser());
					if (null != entityPerson) {
						t.setICONBase64(entityPerson.getIcon());
					}
					if (null != t.getCreateuser() && StringUtils.isNoneBlank(t.getCreateuser())) {
						t.setPerson(business.personFactory().getObject(t.getCreateuser()));
					}
					result.add(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		return result;
	}
}
