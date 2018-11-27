package com.citylife.api.gateway.http.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 自定义熔断ClientHttpResponse
 */
public class JsonClientHttpResponse implements ClientHttpResponse {

    private final String responseBody;

    public JsonClientHttpResponse(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8.name()));
    }

    @Override
    public HttpHeaders getHeaders() {
        //和body中的内容编码一致，否则容易乱码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public int getRawStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getStatusText() {
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    @Override
    public void close() {

    }

}
