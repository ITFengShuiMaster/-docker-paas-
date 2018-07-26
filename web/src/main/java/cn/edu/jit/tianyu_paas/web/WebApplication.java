package cn.edu.jit.tianyu_paas.web;

import cn.edu.jit.tianyu_paas.web.global.AppInterceptor;
import cn.edu.jit.tianyu_paas.web.global.GlobalInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("cn.edu.jit.tianyu_paas.web.mapper")
@EnableWebSocket
@EnableFeignClients
@ComponentScan
public class WebApplication implements WebMvcConfigurer {

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("swagger**")
                .excludePathPatterns("/login.html", "/register.html")
                .excludePathPatterns("/js/**", "/lib/**", "/img/**")
                .excludePathPatterns("/users/login", "/users/register", "/users/active", "users/re-email", "users/phone-code/**")
                .excludePathPatterns("/error");

//        registry.addInterceptor(appInterceptor())
//                .addPathPatterns("/apps/**");
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
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
