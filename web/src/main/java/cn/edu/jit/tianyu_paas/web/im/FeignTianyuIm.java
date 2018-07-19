package cn.edu.jit.tianyu_paas.web.im;

import cn.edu.jit.tianyu_paas.shared.util.TResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("tianyu-paas-im")
@Service
public interface FeignTianyuIm {

    @PostMapping("/users")
    TResult createImUser(@RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("email") String email,
                         @RequestParam("pwd") String pwd, @RequestParam("type") Integer type, @RequestParam("headImg") String headImg);

}
