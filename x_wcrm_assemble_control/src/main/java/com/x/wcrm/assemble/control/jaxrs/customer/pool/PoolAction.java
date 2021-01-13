package com.x.wcrm.assemble.control.jaxrs.customer.pool;

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

@Path("pool")
@JaxrsDescribe("客户公海操作")
public class PoolAction extends StandardJaxrsAction {
	private static Logger logger = LoggerFactory.getLogger(PoolAction.class);

	@JaxrsMethodDescribe(value = "进去啥，出来啥", action = PoolAction.class)
	@GET
	@Path("test/{string}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void Test(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
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
		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}

	@JaxrsMethodDescribe(value = "公海客户分页查询，具备多个字段的like查询(无权限控制)", action = ActionListPoolPagingLike.class)
	@PUT
	@Path("listpaginlike/page/{page}/size/{size}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void listPoolPaginLike(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("页码") @PathParam("page") Integer page, @JaxrsParameterDescribe("每页几条") @PathParam("size") Integer size,
			@JaxrsParameterDescribe("匹配关键字") JsonElement jsonElement) {
		ActionResult<List<ActionListPoolPagingLike.Wo>> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);

		try {
			result = new ActionListPoolPagingLike().execute(effectivePerson, page, size, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}

	@JaxrsMethodDescribe(value = "领取公海客户", action = ActionReceive.class)
	@GET
	@Path("receive/{id}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void receive(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("公海客户ID") @PathParam("id") String id) {
		ActionResult<ActionReceive.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionReceive().execute(effectivePerson, id);

		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}

	@JaxrsMethodDescribe(value = "分配用户", action = ActionDistribute.class)
	@PUT
	@Path("distribute/{id}")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void distribute(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("公海客户ID") @PathParam("id") String id, @JaxrsParameterDescribe("人员列表，distinguishName") JsonElement jsonElement) {
		ActionResult<ActionDistribute.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionDistribute().execute(effectivePerson, id, jsonElement);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getDefaultActionResultResponse(result));
	}

}
