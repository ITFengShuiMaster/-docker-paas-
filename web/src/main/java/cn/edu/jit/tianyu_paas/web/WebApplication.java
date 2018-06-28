package cn.edu.jit.tianyu_paas.web;

import cn.edu.jit.tianyu_paas.web.global.TInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@MapperScan("cn.edu.jit.tianyu_paas.web.mapper")
public class WebApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tInterceptor())
                .addPathPatterns("/**")
                // 不拦截用户登录，注册
                .excludePathPatterns("/user/login", "/user/register")
                //这一条要加上，因为拦截器拦截请求时会定向到这里，如果不加则会返回200
                .excludePathPatterns("/error");
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
    public TInterceptor tInterceptor() {
        return new TInterceptor();
    }
}
