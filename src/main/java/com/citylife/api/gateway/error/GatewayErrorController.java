package com.citylife.api.gateway.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.citylife.common.component.TraceHelper;
import com.citylife.common.constants.SystemMessageConsts;
import com.citylife.common.message.MessageResolver;
import com.citylife.common.model.ResultEntity;

/**
 * 自定义异常错误处理器
 */
@RequestMapping("/gateway")
@RestController
public class GatewayErrorController implements ErrorController {

  public GatewayErrorController() {
    super();
  }

  @Autowired
  private MessageResolver messageResolver;

  @Autowired
  private TraceHelper traceHelper;

  @Override
  public String getErrorPath() {
    return "/gateway/error";
  }

  @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/error")
  public ResultEntity<?> error(HttpServletRequest request) {
    ResultEntity<?> result = ResultEntity.failure(ResultEntity.SYSTEM_ERROR,
        messageResolver.error(SystemMessageConsts.SYSTEM_ERROR_UNEXPECTED));
    result.setTraceId(traceHelper.getTraceId(request));
    return result;
  }

}
