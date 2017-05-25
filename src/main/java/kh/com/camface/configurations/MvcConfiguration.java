package kh.com.camface.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;

@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/development").setViewName("swagger-ui");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/").resourceChain(true).addResolver(new GzipResourceResolver());
        registry.addResourceHandler("/static/webjars/**").addResourceLocations("classpath:/static/webjars/").resourceChain(true).addResolver(new GzipResourceResolver());
        registry.addResourceHandler("/webjars/**").addResourceLocations("/resources/").resourceChain(true).addResolver(new GzipResourceResolver());
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(true).addResolver(new GzipResourceResolver());
    }

}
