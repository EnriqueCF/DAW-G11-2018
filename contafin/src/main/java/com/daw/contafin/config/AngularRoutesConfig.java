package com.daw.contafin.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AngularRoutesConfig {

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/new").setViewName("forward:/new/index.html");
                registry.addViewController("/new/").setViewName("forward:/new/index.html");
                registry.addViewController("/new/home").setViewName("forward:/new/index.html");
                registry.addViewController("/new/User/Profile").setViewName("forward:/new/index.html");
                registry.addViewController("/new/User/Configuration").setViewName("forward:/new/index.html");
                registry.addViewController("/new/User/Goal").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Admin/Home").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Admin/UserData").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Admin/Content").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Unit/*/Lessons").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Unit/*/Lessons/*/Exercise").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Unit/*/Lesson/*/lessonCompleted").setViewName("forward:/new/index.html");
                registry.addViewController("/new/Error").setViewName("forward:/new/index.html");
                registry.addViewController("/new/ContinueLesson").setViewName("forward:/new/index.html");
            }
        };
    }
}