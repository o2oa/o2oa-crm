package com.x.wcrm.assemble.control.jaxrs.common;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.x.base.core.project.annotation.JaxrsDescribe;
import com.x.base.core.project.annotation.JaxrsMethodDescribe;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.HttpMediaType;
import com.x.base.core.project.jaxrs.ResponseFactory;
import com.x.base.core.project.jaxrs.WrapString;
import com.x.base.core.project.jaxrs.WrapStringList;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;

@Path("common")
@JaxrsDescribe("crm的共用服务等.")
public class CommonAction extends BaseAction {

        private static Logger logger = LoggerFactory.getLogger(CommonAction.class);

        @JaxrsMethodDescribe(value = "列出模块Id", action = CommonAction.class)
        @GET
        @Path("listmoduleId")
        @Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
        @Consumes(MediaType.APPLICATION_JSON)
        public void listModuleId(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
                ActionResult<WrapStringList> result = new ActionResult<>();
                EffectivePerson effectivePerson = this.effectivePerson(request);
                try {
                        result.setData(_getModuleIdList());
                } catch (Exception e) {
                        logger.error(e, effectivePerson, request, null);
                        result.error(e);
                }
                asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
        }

        @JaxrsMethodDescribe(value = "列出模块名称（中文名称）", action = CommonAction.class)
        @GET
        @Path("listmodulevalue")
        @Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
        @Consumes(MediaType.APPLICATION_JSON)
        public void listModuleValue(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
                ActionResult<WrapStringList> result = new ActionResult<>();
                EffectivePerson effectivePerson = this.effectivePerson(request);
                try {
                        result.setData(_getModuleValueList());
                } catch (Exception e) {
                        logger.error(e, effectivePerson, request, null);
                        result.error(e);
                }
                asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
        }

        @JaxrsMethodDescribe(value = "随机生成一个uuid", action = CommonAction.class)
        @GET
        @Path("randomuuid")
        @Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
        @Consumes(MediaType.APPLICATION_JSON)
        public void creatRandomUUID(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
                ActionResult<WrapString> result = new ActionResult<>();
                EffectivePerson effectivePerson = this.effectivePerson(request);
                try {
                        WrapString _wrapString = new WrapString();
                        _wrapString.setValue(UUID.randomUUID().toString());
                        result.setData(_wrapString);
                } catch (Exception e) {
                        logger.error(e, effectivePerson, request, null);
                        result.error(e);
                }
                asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
        }

        @JaxrsMethodDescribe(value = "列出跟进记录可用类型", action = CommonAction.class)
        @GET
        @Path("listrecordtype")
        @Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
        @Consumes(MediaType.APPLICATION_JSON)
        public void listRecordType(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
                ActionResult<WrapStringList> result = new ActionResult<>();
                EffectivePerson effectivePerson = this.effectivePerson(request);
                try {
                        result.setData(_getRecordTypeValueList());
                } catch (Exception e) {
                        logger.error(e, effectivePerson, request, null);
                        result.error(e);
                }
                asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
        }

        @JaxrsMethodDescribe(value = "当前用户，是否具有管理员权限", action = IsManagerAction.class)
        @GET
        @Path("isManager")
        @Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
        @Consumes(MediaType.APPLICATION_JSON)
        public void isManager(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request) {
                ActionResult<IsManagerAction.Wo> result = new ActionResult<>();
                EffectivePerson effectivePerson = this.effectivePerson(request);
                try {
                        result = new IsManagerAction().execute(effectivePerson);
                } catch (Exception e) {
                        logger.error(e, effectivePerson, request, null);
                        result.error(e);
                }
                asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
        }

}
