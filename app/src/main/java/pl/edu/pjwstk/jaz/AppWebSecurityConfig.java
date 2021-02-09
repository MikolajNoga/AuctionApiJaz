package pl.edu.pjwstk.jaz;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppWebSecurityConfig {

    @Bean
    public FilterRegistrationBean<ExampleFilter> exampleFilter(UserSession userSession){
        FilterRegistrationBean<ExampleFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ExampleFilter(userSession));
        registrationBean.addUrlPatterns("/auth0/*");

        return registrationBean;
    }
}
