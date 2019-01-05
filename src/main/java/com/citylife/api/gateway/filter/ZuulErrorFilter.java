package com.citylife.api.gateway.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.citylife.common.logging.AdminLogger;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


public class ZuulErrorFilter extends ZuulFilter {

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext ctx = RequestContext.getCurrentContext();
    Throwable throwable = ctx.getThrowable();
    AdminLogger.error("this is a ErrorFilter :" + throwable.getCause().getMessage(), throwable);
    ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    ctx.set("error.exception", throwable.getCause());
    return null;
  }

  @Override
  public String filterType() {
    return FilterConstants.ERROR_TYPE;
  }

  @Override
  public int filterOrder() {
    return 10;
  }

}
