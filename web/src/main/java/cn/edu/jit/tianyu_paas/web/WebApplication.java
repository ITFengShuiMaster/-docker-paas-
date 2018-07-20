package cn.edu.jit.tianyu_paas.web;

import cn.edu.jit.tianyu_paas.web.global.AppInterceptor;
import cn.edu.jit.tianyu_paas.web.global.GlobalInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("cn.edu.jit.tianyu_paas.web.mapper")
@EnableFeignClients
public class WebApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("swagger**")
                .excludePathPatterns("/user/login", "/user/register")
                .excludePathPatterns("/error");

//        registry.addInterceptor(appInterceptor())
//                .addPathPatterns("/apps/**");
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 加上这个，拦截器才会被spring管理，才可以获取配置文件的属性
     * 另外addInterceptors方法中需要使用这个Bean。不能new
     */
    @Bean
    public GlobalInterceptor globalInterceptor() {
        return new GlobalInterceptor();
    }

    @Bean
    public AppInterceptor appInterceptor() {
        return new AppInterceptor();
    }
}
