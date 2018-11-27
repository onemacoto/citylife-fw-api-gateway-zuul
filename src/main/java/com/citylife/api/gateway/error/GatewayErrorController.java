package com.citylife.api.gateway.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citylife.api.gateway.http.client.bean.JsonResponseBean;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义异常错误处理器
 */
@RequestMapping("/gateway")
@RestController
public class GatewayErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getErrorPath() {
        return "/gateway/error";
    }

    @GetMapping("/error")
    public JsonResponseBean error(HttpServletRequest request) {
        if (logger.isErrorEnabled()) {
            logger.error("网关层发生未知异常");
        }
        return JsonResponseBean.buildCcfResponseBean(JsonResponseBean.GW_INTERNAL_ERROR_CODE, "系统内部异常, 请通知系统管理员进行排查.");
    }

}
