package cn.edu.jit.tianyu_paas.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
//@EnableEurekaClient
@MapperScan("cn.edu.jit.tianyu_paas.im.mapper")
@EnableWebSocket
public class ImApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class, args);
    }
}