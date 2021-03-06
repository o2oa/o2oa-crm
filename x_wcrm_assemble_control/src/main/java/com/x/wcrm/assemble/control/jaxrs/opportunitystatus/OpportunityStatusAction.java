package com.x.wcrm.assemble.control.jaxrs.opportunitystatus;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.x.base.core.project.http.WrapOutId;
import com.x.base.core.project.jaxrs.ResponseFactory;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;

@Path("opportunitystatus")
@JaxrsDescribe("商机状态操作")
public class OpportunityStatusAction extends StandardJaxrsAction {

	private static Logger logger = LoggerFactory.getLogger(OpportunityStatusAction.class);

	@JaxrsMethodDescribe(value = "进去啥，出来啥", action = OpportunityStatusAction.class)
	@GET
	@Path("test/{string}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void opportunityStatusTest(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("测试文字") @PathParam("string") String string) {
		ActionResult<String> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			String resultStr = "return result:" + string;
			result.setData(resultStr);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

	@JaxrsMethodDescribe(value = "创建商机状态", action = ActionCreate.class)
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

	@JaxrsMethodDescribe(value = "获取所有商机状态（创建时间倒序排序）", action = ActionList.class)
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

	@JaxrsMethodDescribe(value = "获取所有商机状态（创建时间倒序排序）", action = ActionListByTypeId.class)
	@GET
	@Path("listbytypeid/{typeid}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listByTypeId(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("商机组id") @PathParam("typeid") String typeid) {
		ActionResult<List<ActionListByTypeId.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionListByTypeId().execute(effectivePerson, typeid);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}
	
	@JaxrsMethodDescribe(value = "根据商机状态id，删除商机状态.", action = ActionDelete.class)
	@DELETE
	@Path("{id}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("商机状态id") @PathParam("id") String id) {
		ActionResult<WrapOutId> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionDelete().execute(effectivePerson, id);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}


	//
	//	@JaxrsMethodDescribe(value = "根据商机状态组id，获取商机状态组对象。单个", action = ActionGet.class)
	//	@GET
	//	@Path("get/{id}")
	//	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public void getById(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
	//			@JaxrsParameterDescribe("id") @PathParam("id") String id) {
	//		ActionResult<ActionGet.Wo> result = new ActionResult<>();
	//		EffectivePerson effectivePerson = this.effectivePerson(request);
	//		try {
	//			result = new ActionGet().execute(effectivePerson, id);
	//		} catch (Exception e) {
	//			logger.error(e, effectivePerson, request, null);
	//			result.error(e);
	//		}
	//		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	//	}
	//	
	//	@JaxrsMethodDescribe(value = "获取所有商机状态组（创建时间倒序排序）", action = ActionList.class)
	//	@GET
	//	@Path("list")
	//	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public void list(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
	//		ActionResult<List<ActionList.Wo>> result = new ActionResult<>();
	//		EffectivePerson effectivePerson = this.effectivePerson(request);
	//		try {
	//			result = new ActionList().execute(effectivePerson);
	//		} catch (Exception e) {
	//			logger.error(e, effectivePerson, request, null);
	//			result.error(e);
	//		}
	//		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	//	}

	//
	//	@JaxrsMethodDescribe(value = "商机分页查询，具备多个字段的like查询(无权限控制)", action = ActionListPagingLike.class)
	//	@PUT
	//	@Path("listpaginlike/page/{page}/size/{size}")
	//	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public void listPaginLike(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
	//			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
	//			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
	//		ActionResult<List<ActionListPagingLike.Wo>> result = new ActionResult<>();
	//		EffectivePerson effectivePerson = this.effectivePerson(request);
	//
	//		try {
	//			result = new ActionListPagingLike().Execute_Paging_like(effectivePerson, page, size, jsonElement);
	//		} catch (Exception e) {
	//			logger.error(e, effectivePerson, request, null);
	//			result.error(e);
	//		}
	//		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	//	}

}
