package com.citylife.api.gateway.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.citylife.api.gateway.http.client.JsonClientHttpResponse;
import com.citylife.api.gateway.http.client.bean.JsonResponseBean;

/**
 * Hystrix熔断错误处理器
 */
@Component
public class GatewayFallbackProvider implements FallbackProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (logger.isErrorEnabled()) {
            logger.error(route, cause);
        }
        String responseDesc = "服务[" + route + "]过载, 请稍后再试.";
        String responseBody = JsonResponseBean.buildCcfResponseBeanJsonString(JsonResponseBean.GW_HYSTRIX_FALLBACK_CODE, responseDesc);
        return new JsonClientHttpResponse(responseBody);
    }
}
