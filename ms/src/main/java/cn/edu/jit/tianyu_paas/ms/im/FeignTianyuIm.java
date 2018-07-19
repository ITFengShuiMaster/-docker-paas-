package cn.edu.jit.tianyu_paas.ms.im;

import cn.edu.jit.tianyu_paas.shared.util.TResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@FeignClient("tianyu-paas-im")
@Service
public interface FeignTianyuIm {

    @GetMapping("/users/{userId}")
    TResult getUserInfo(@PathVariable("userId") String userId);

    @PostMapping("/users")
    TResult createCustomerService(@RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("email") String email,
                                  @RequestParam("pwd") String pwd, @RequestParam("type") Integer type, @RequestParam("headImg") String headImg);

    @PutMapping("/users")
    TResult updateCustomerService(@RequestParam("userId") long userId, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("email") String email,
                                  @RequestParam("pwd") String pwd, @RequestParam("type") Integer type, @RequestParam("headImg") String headImg);

    @DeleteMapping("/users/{userId}")
    TResult deleteUser(@PathVariable("userId") long userId);
}
