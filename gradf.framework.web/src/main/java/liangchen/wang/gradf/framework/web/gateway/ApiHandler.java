package liangchen.wang.gradf.framework.web.gateway;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author LiangChen.Wang
 */
public enum ApiHandler {
    /**
     *
     */
    INSTANCE;
    private static final String API = "api";
    private static final String CONTENT = "application/json";
    private static final String CHARSETNAME = "UTF-8";
    private static final String GATEWAY = "gateway/";
    private static final int INDEXOFFSET = GATEWAY.length();

    public void handle(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        String requestURI = request.getRequestURI();
        int index = requestURI.lastIndexOf(GATEWAY);
        index += INDEXOFFSET;
        String apiName = requestURI.substring(index);
        if (StringUtil.INSTANCE.isBlank(apiName)) {
            ResponseUtil.createResponse(response).error("API未指定").flush();
            return;
        }
        ApiContainer.ApiTarget apiTarget = ApiContainer.INSTANCE.getApiTarget(apiName);
        if (null == apiTarget) {
            ResponseUtil.createResponse(response).error("API不存在").flush();
            return;
        }
        String requestMethod = request.getMethod();
        if (!apiTarget.getRequestMethod().name().equalsIgnoreCase(requestMethod)) {
            ResponseUtil.createResponse(response).error(String.format("请求方法%s错误", requestMethod)).flush();
            return;
        }
        String parameter = getRequestBody(request);
        Object[] args = buildParams(apiTarget, parameter, request);
        Object result = apiTarget.run(args);
        if (null != result) {
            ResponseUtil.createResponse(response).data(result).flush();
        }
    }

    private Object[] buildParams(ApiContainer.ApiTarget apiTarget, String parameter, HttpServletRequest request) {
        return new Object[0];
        /* 待去除fastjson
	    JSONObject json = JSON.parseObject(parameter);
        if(null==json){
            json = new JSONObject();
        }
        //获取access_token
        String access_token = request.getParameter("access_token");
        if(StringUtil.INSTANCE.isBlank(access_token)){
            access_token = request.getHeader("access_token");
        }
        if(StringUtil.INSTANCE.isBlank(access_token)){
            access_token = json.getString("access_token");
        }

        Class<?>[] parameterTypes = apiTarget.getParameterTypes();
        String[] parameterNames = apiTarget.getParameterNames();
        Object[] args = new Object[parameterTypes.length];
        Class<?> parameterType;
        String parameterName;
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterType = parameterTypes[i];
            if (parameterType.isAssignableFrom(HttpServletRequest.class)) {
                args[i] = request;
                continue;
            }
            if (parameterType.isAssignableFrom(AccessToken.class) && StringUtil.INSTANCE.isNotBlank(access_token)) {
                //TODO 动态Key待修改
                args[i] = AccessTokenUtil.INSTANCE.parse(access_token,"-------");
                continue;
            }
            parameterName = parameterNames[i];
            args[i] = json.getObject(parameterName, parameterType);
        }
        return args;
         */
    }

    private String getRequestBody(HttpServletRequest request) {
        try (InputStream inputStream = request.getInputStream()) {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteOutputStream.write(buffer, 0, length);
            }
            return byteOutputStream.toString(CHARSETNAME);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

}
