package com.citylife.api.gateway.entrypoint;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.citylife.common.component.TraceHelper;
import com.citylife.common.constants.SystemMessageConsts;
import com.citylife.common.message.MessageResolver;
import com.citylife.common.model.ResultEntity;

public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	private MessageResolver messageResolver;

	@Autowired
	private TraceHelper traceHelper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		doHandle(request, response, authException);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws ServletException {
		
		ResultEntity<?> result = ResultEntity.failure(ResultEntity.SYSTEM_ERROR,
				messageResolver.error(SystemMessageConsts.SYSTEM_ERROR_OAuth2));
        result.setTraceId(traceHelper.getTraceId(request));
        String responseBody = JSON.toJSONString(result);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
			StreamUtils.copy(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8.name())), response.getOutputStream());
		} catch (Exception e) {
			throw new ServletException(e);
		}
 	}
	
}
