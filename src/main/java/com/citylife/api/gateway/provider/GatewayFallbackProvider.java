package com.citylife.api.gateway.provider;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.citylife.api.gateway.http.client.JsonClientHttpResponse;
import com.citylife.common.component.TraceHelper;
import com.citylife.common.constants.SystemMessageConsts;
import com.citylife.common.logging.AdminLogger;
import com.citylife.common.message.MessageResolver;
import com.citylife.common.model.ResultEntity;

/**
 * Hystrix熔断错误处理器
 */
public class GatewayFallbackProvider implements FallbackProvider {
   @Autowired
   private MessageResolver messageResolver;
   
   @Autowired
   private TraceHelper traceHelper;
  
  
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        AdminLogger.error(String.format("熔断错误发生Route[%s]", route), cause);
        ResultEntity<?> result = ResultEntity.failure(ResultEntity.SYSTEM_ERROR, messageResolver.error(SystemMessageConsts.SYSTEM_ERROR_FALLBACK));
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        result.setTraceId(traceHelper.getTraceId(httpRequest));
        String responseBody = JSON.toJSONString(result);
        return new JsonClientHttpResponse(responseBody);
    }
}
