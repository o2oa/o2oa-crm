package com.x.wcrm.assemble.control.jaxrs.leads;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

@Path("leads")
@JaxrsDescribe("线索操作")
public class LeadsAction extends StandardJaxrsAction {

	private static Logger logger = LoggerFactory.getLogger(LeadsAction.class);

	@JaxrsMethodDescribe(value = "进去啥，出来啥", action = LeadsAction.class)
	@GET
	@Path("test/{leads}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listWithJob(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("任务标识") @PathParam("leads") String leads) {
		ActionResult<String> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			String resultStr = "return result:" + leads;
			result.setData(resultStr);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "创建线索", action = ActionCreate.class)
	@POST
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("create")
	public void create(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request, JsonElement jsonElement) {
		ActionResult<ActionCreate.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionCreate().execute(effectivePerson, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, jsonElement);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "根据线索id，删除线索，无其他的关联删除动作", action = ActionDelete.class)
	@DELETE
	@Path("{id}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("线索id") @PathParam("id") String id) {
		ActionResult<ActionDelete.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionDelete().execute(effectivePerson, id);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "根据线索id，获取线索对象。单个", action = LeadsAction.class)
	@GET
	@Path("get/{leadsid}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void getById(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("任务标识") @PathParam("leadsid") String leadsid) {
		ActionResult<ActionGet.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionGet().execute(effectivePerson, leadsid);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取所有线索", action = ActionList.class)
	@GET
	@Path("list")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void list(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
		ActionResult<List<ActionList.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionList().execute(effectivePerson);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取当前登录人的所有线索", action = ActionList.class)
	@GET
	@Path("mylist")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void mylist(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
		ActionResult<List<ActionList.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionList().MyExecute(effectivePerson);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取所有线索，按创建时间倒序排列", action = ActionList.class)
	@GET
	@Path("listbyorder")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listbyorder(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
		ActionResult<List<ActionList.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionList().Execute_OrderByCreateTime(effectivePerson);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "线索分页查询，具备多个字段的like查询(无权限控制)", action = ActionListPaging.class)
	@PUT
	@Path("listpaginlike/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listPaginLike(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListPaging.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);

		try {
			result = new ActionListPaging().Execute_Paging_like(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "更新线索", action = ActionUpdate.class)
	@PUT
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("update/{leadsid}/iskeep/{isKeepOriginalData}")
	public void update(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("线索ID") @PathParam("leadsid") String leadsid,
			@JaxrsParameterDescribe("是否保留原数据（true，false）") @PathParam("isKeepOriginalData") boolean isKeepOriginalData, JsonElement jsonElement) {
		ActionResult<ActionUpdate.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionUpdate().execute(effectivePerson, leadsid, isKeepOriginalData, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, jsonElement);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "线索转为客户", action = ActionTransformToCustomer.class)
	@GET
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("transformtocustomer/{leadsid}")
	public void transformToCustomer(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("任务标识") @PathParam("leadsid") String leadsid) {

		ActionResult<ActionTransformToCustomer.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionTransformToCustomer().execute(effectivePerson, leadsid);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "线索的转移服务.", action = ActionTransfer.class)
	@PUT
	@Path("transfer/id/{id}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void transfer(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("客户ID（uuid）") @PathParam("id") String id, @JaxrsParameterDescribe("传入的参数") JsonElement jsonElement) {
		ActionResult<ActionTransfer.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);

		try {
			result = new ActionTransfer().execute(effectivePerson, id, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取所有下属线索（递归）", action = ActionListBySubPersonNested.class)
	@PUT
	@Path("listsub/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void ListNestedSubPerson(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListBySubPersonNested.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListBySubPersonNested().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取我负责的线索", action = ActionListByMyDuty.class)
	@PUT
	@Path("listmyduty/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void ListMyDuty(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListByMyDuty.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListByMyDuty().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取我负责，和我下属负责的线索", action = ActionListMyDutyAndSubNestedDuty.class)
	@PUT
	@Path("listmydutyandsubnestedduty/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void ListMyDutyAndSubNestedDuty(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListMyDutyAndSubNestedDuty.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListMyDutyAndSubNestedDuty().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取我的所有线索（包括1:我自己负责或者我下属负责的线索；2;我自己作为线索团队成员的线索）", action = ActionListMyDutyAndSubNestedDuty.class)
	@PUT
	@Path("listallmy/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void ListAllMy(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListMyDutyAndSubNestedDuty.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListMyDutyAndSubNestedDuty().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "获取我负责，和我下属负责已转化的线索", action = ActionListMyHasTransform.class)
	@PUT
	@Path("listmyhastransform/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void ListMyHasTransform(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListMyHasTransform.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListMyHasTransform().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

}
