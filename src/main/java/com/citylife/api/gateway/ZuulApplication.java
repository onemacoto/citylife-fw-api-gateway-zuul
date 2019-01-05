package com.citylife.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.stream.HystrixStreamAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.citylife.api.gateway.provider.GatewayFallbackProvider;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

import brave.http.HttpTracing;
import brave.okhttp3.TracingInterceptor;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

@SpringBootApplication(exclude = {HystrixStreamAutoConfiguration.class})
@EnableZuulProxy
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class ZuulApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZuulApplication.class, args);
  }

  /**
   * Hystrix端点配置
   *
   * @return ServletRegistrationBean<HystrixMetricsStreamServlet>
   */
  @Bean(name = "HystrixMetricsStreamServletBean")
  public ServletRegistrationBean<HystrixMetricsStreamServlet> servletTurbineRegistrationBean() {
    ServletRegistrationBean<HystrixMetricsStreamServlet> registration = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet(), "/actuator/hystrix.stream");
    registration.setName("HystrixMetricsStreamServlet");
    registration.setLoadOnStartup(1);
    return registration;
  }

  /**
   * 跨域权限设置
   *
   * @return CorsFilter
   */
  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedMethod(HttpMethod.OPTIONS.name());
    config.addAllowedMethod(HttpMethod.HEAD.name());
    config.addAllowedMethod(HttpMethod.GET.name());
    config.addAllowedMethod(HttpMethod.PUT.name());
    config.addAllowedMethod(HttpMethod.POST.name());
    config.addAllowedMethod(HttpMethod.DELETE.name());
    config.addAllowedMethod(HttpMethod.PATCH.name());
    config.addAllowedHeader(CorsConfiguration.ALL);
    config.addAllowedOrigin(CorsConfiguration.ALL);
    config.addAllowedMethod(CorsConfiguration.ALL);
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  /**
   * 底层用OkHttp3Client发起远程调用
   * <p>
   * 此处让SleuthTracing数据完整
   *
   * @param httpTracing
   * @return
   */
  @Bean
  public OkHttpClient.Builder okHttpClientBuilder(HttpTracing httpTracing) {
    return new OkHttpClient.Builder().dispatcher(new Dispatcher(httpTracing.tracing().currentTraceContext().executorService(new Dispatcher().executorService()))).addNetworkInterceptor(
        TracingInterceptor.create(httpTracing));
  }

  // @Bean
  // public ZuulErrorFilter errorFilter() {
  // return new ZuulErrorFilter();
  // }

  public GatewayFallbackProvider gatewayFallbackProvider() {
    GatewayFallbackProvider bean = new GatewayFallbackProvider();
    return bean;

  }

}
