package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.x.base.core.project.tools.ListTools;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.assemble.control.ThisApplication;
import com.x.wcrm.assemble.control.complex.SimpleKV;
import com.x.wcrm.assemble.control.wrapin.ListStatisticPagingWi;

//全部客户按照月份
public class Action_Customer_CountByMonth extends BaseAction {

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Business business = new Business(emc);
			List<String> _persons = new ArrayList<String>();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ListTools.isEmpty(wi.getUnitList()) && ListTools.isEmpty(wi.getPersonNameList())) {
				_persons = permissionServiceBase.getPersonList_MyAndSubNested(ThisApplication.context(), effectivePerson);
			}else{
				// 人员列表，组织列表有一项或者多项不为空。根据传入的组织和人员进行计算
				List<String> _persons_p = this.convertToPerson(business, wi.getPersonNameList());
				List<String> _persons_u = this.convertToPerson(business, wi.getUnitList());
				_persons = ListTools.add(_persons_p, true, true, _persons_u);
			}
			//List<String> _distinguishNameList = permissionServiceBase.getPersonList_MyAndSubNested(ThisApplication.context(), effectivePerson);

			List<SimpleKV> os = business.customerStatisticFactory().count_by_month(_persons, null, sdf.parse(wi.getBegintime()), sdf.parse(wi.getEndtime()));
			List<Wo> wos = new ArrayList<>();
			for (SimpleKV simpleKV : os) {
				Wo o = new Wo();
				String _tmpDate = formateYYYYMM(simpleKV.getKey());
				o.setMonth(_tmpDate);
				o.setCount(simpleKV.getValue());
				o.setRank(convertToRankByDate(_tmpDate));
				wos.add(o);
			}

			//排序
			wos = wos.stream().sorted(Comparator.comparingInt(Wo::getRank)).collect(Collectors.toList());

			result.setData(wos);
			return result;
		}

	}

	public static class Wi extends ListStatisticPagingWi {

	}

	static class Wo extends GsonPropertyObject {
		private String month;
		String count;
		private int rank;	//排序

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

	}

	/**
	 * 201910，或者，20198 格式化为“YYYY-MM”
	 */
	public static String formateYYYYMM(String dateStr) {

		String result = "";
		System.out.println("len=="+StringUtils.length(dateStr));
		if (StringUtils.length(dateStr) == 5 || StringUtils.length(dateStr) == 6) {
			if (StringUtils.length(dateStr) == 5) {
				result = StringUtils.left(dateStr, 4);
				result = result + "-" + "0" + StringUtils.right(dateStr, 1);
			}

			if (StringUtils.length(dateStr) == 6) {
				result = StringUtils.left(dateStr, 4);
				result = result + "-" + StringUtils.right(dateStr, 2);
			}

			if (StringUtils.isBlank(result)) {
				return dateStr;
			} else {
				return result;
			}

		} else {
			return dateStr;
		}

	}

	public static int convertToRankByDate(String formateDateStr) {
		int result = 0;
		String[] stringArr = formateDateStr.split("-");
		result = Integer.valueOf(stringArr[0]) * 12 + Integer.valueOf(stringArr[1]);
		return result;
	}

//		public static void main(String[] args) {
//			Action_Customer_CountByMonth me = new Action_Customer_CountByMonth();
//			System.out.println("Action_Customer_CountByMonth.main():" + me.formateYYYYMM("201912"));
//		}
}
