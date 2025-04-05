package org.it.questforumapp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<BrowserDetectionFilter> browserDetectionFilter() {
        FilterRegistrationBean<BrowserDetectionFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new BrowserDetectionFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("BrowserDetectionFilter");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
