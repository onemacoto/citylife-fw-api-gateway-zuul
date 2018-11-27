package com.citylife.api.gateway.http.client.bean;

import com.alibaba.fastjson.JSON;

/**
 * 为了便于前端显示 构建MO体
 *
 * @author cosco
 */
public class JsonResponseBean {

    /**
     * 网关发生熔断错误码
     */
    public static final String GW_HYSTRIX_FALLBACK_CODE = "MSG.FK8888";

    /**
     * 网关内部异常错误码
     */
    public static final String GW_INTERNAL_ERROR_CODE = "MSG.FK8000";

    /**
     * 根据responseCode和responseDesc构建MO体
     *
     * @param responseCode
     * @param responseDesc
     * @return 类似MO的CcfResponseBean
     */
    public static JsonResponseBean buildCcfResponseBean(String responseCode, String responseDesc) {
        JsonResponseBean ccfResponseBean = new JsonResponseBean();
        JsonResponseBean.Body body = new JsonResponseBean.Body();
        JsonResponseBean.Parameter parameter = new JsonResponseBean.Parameter();
        parameter.setBody("empty");
        
        JsonResponseBean.Response response = new JsonResponseBean.Response();
        response.setParameter(parameter);

        JsonResponseBean.Request request = new JsonResponseBean.Request();
        request.setParameter(parameter);

        body.setResponse(response);
        body.setRequest(request);

        JsonResponseBean.Header header = new JsonResponseBean.Header();
        header.setReturn_code(Boolean.FALSE.toString());
        header.setResponse_code(responseCode);
        header.setResponse_desc(responseDesc);

        ccfResponseBean.setBody(body);
        ccfResponseBean.setHeader(header);
        return ccfResponseBean;
    }

    /**
     * 根据responseCode和responseDesc构建MO Json字符串
     *
     * @param responseCode
     * @param responseDesc
     * @return 类似MO的Json字符串
     */
    public static String buildCcfResponseBeanJsonString(String responseCode, String responseDesc) {
        JsonResponseBean ccfResponseBean = buildCcfResponseBean(responseCode, responseDesc);
        return JSON.toJSONString(ccfResponseBean);
    }

    private Header header;
    private Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Header {
        private String return_code;
        private String response_code;
        private String response_desc;

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getResponse_code() {
            return response_code;
        }

        public void setResponse_code(String response_code) {
            this.response_code = response_code;
        }

        public String getResponse_desc() {
            return response_desc;
        }

        public void setResponse_desc(String response_desc) {
            this.response_desc = response_desc;
        }
    }

    public static class Body {

        private Response response;
        private Request request;

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        public Request getRequest() {
            return request;
        }

        public void setRequest(Request request) {
            this.request = request;
        }
    }

    public static class Response {

        private Parameter parameter;

        public Parameter getParameter() {
            return parameter;
        }

        public void setParameter(Parameter parameter) {
            this.parameter = parameter;
        }
    }

    public static class Request {
        private Parameter parameter;

        public Parameter getParameter() {
            return parameter;
        }

        public void setParameter(Parameter parameter) {
            this.parameter = parameter;
        }
    }

    public static class Parameter {
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
