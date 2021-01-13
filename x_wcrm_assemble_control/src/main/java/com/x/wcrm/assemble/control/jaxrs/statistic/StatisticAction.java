package com.x.wcrm.assemble.control.jaxrs.statistic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonElement;
import com.x.base.core.project.annotation.JaxrsDescribe;
import com.x.base.core.project.annotation.JaxrsMethodDescribe;
import com.x.base.core.project.annotation.JaxrsParameterDescribe;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.HttpMediaType;
import com.x.base.core.project.jaxrs.ResponseFactory;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;

@Path("statistic")
@JaxrsDescribe("统计操作")
public class StatisticAction extends StandardJaxrsAction {

	private static Logger logger = LoggerFactory.getLogger(StatisticAction.class);

	@JaxrsMethodDescribe(value = "带有时间范围 客户分页查询，具备多个字段的like查询(有权限)", action = ActionListPagingLikeTimeRang.class)
	@PUT
	@Path("listpaginlike/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listPaginLike(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListPagingLikeTimeRang.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListPagingLikeTimeRang().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "带有时间范围,具备多个字段的like查询(有权限)", action = ActionCountLikeTimeRang.class)
	@PUT
	@Path("countlike")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void countLike(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("匹配关键字JSON") JsonElement jsonElement) {
		ActionResult<List<ActionCountLikeTimeRang.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionCountLikeTimeRang().execute(effectivePerson, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "按照省份分组，列出每个省份的客户数量", action = Action_Customer_CountByProvince.class)
	@PUT
	@Path("countcustomerbyprovince")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void countCustomerByProvince(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
										@JaxrsParameterDescribe("匹配关键字JSON") JsonElement jsonElement) {
		ActionResult<List<Action_Customer_CountByProvince.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new Action_Customer_CountByProvince().execute(effectivePerson, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "按照行业分组，列出每个行业的客户数量（带有权限）", action = Action_Customer_CountByIndustry.class)
	@PUT
	@Path("countcustomerbyindustry")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void countCustomerByIndustry(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("匹配关键字JSON") JsonElement jsonElement) {
		ActionResult<List<Action_Customer_CountByIndustry.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new Action_Customer_CountByIndustry().execute(effectivePerson, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "按照月份分组，列出每个月份的客户数量（带有权限）", action = Action_Customer_CountByMonth.class)
	@PUT
	@Path("countcustomerbymonth")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void countCustomerByMonth(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("匹配关键字JSON") JsonElement jsonElement) {
		ActionResult<List<Action_Customer_CountByMonth.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new Action_Customer_CountByMonth().execute(effectivePerson, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "带有next时间范围 客户分页查询，具备多个字段的like查询(有权限)【根据创建时间，某一时间段内，我和我下属负责的客户列表】", action = ActionListPagingLikeNextTimeRang.class)
	@PUT
	@Path("listnexttimepaginlike/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listNextTimePaginLike(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListPagingLikeNextTimeRang.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListPagingLikeNextTimeRang().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "客户总量分析", action = ActionCustomerAnalyzeByAll.class)
	@PUT
	@Path("customeranalyzebyall/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void customerAnalyzeByAll(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request, @JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionCustomerAnalyzeByAll.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionCustomerAnalyzeByAll().execute(effectivePerson, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}
}
