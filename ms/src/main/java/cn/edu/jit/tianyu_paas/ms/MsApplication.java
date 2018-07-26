package cn.edu.jit.tianyu_paas.ms;

import cn.edu.jit.tianyu_paas.ms.global.GlobalInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("cn.edu.jit.tianyu_paas.ms.mapper")
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients
public class MsApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MsApplication.class, args);
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/js/**", "/lib/**", "/img/**")
                .excludePathPatterns("/error");
    }

    @Bean
    public GlobalInterceptor globalInterceptor() {
        return new GlobalInterceptor();
    }

    /**
     * 使restTemplate开启负载均衡能力，如果有多个微服务，会自动均衡调用
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}