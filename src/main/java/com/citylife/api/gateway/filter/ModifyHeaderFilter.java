package com.citylife.api.gateway.filter;

import java.util.Map;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class ModifyHeaderFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			RequestContext ctx = RequestContext.getCurrentContext();
			ctx.addZuulRequestHeader("User-Token", (String) Map.class.cast(auth.getPrincipal()).get("headerUser"));
		} catch (Exception igore) {

		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return FilterConstants.RIBBON_ROUTING_FILTER_ORDER - 1;
	}

}
