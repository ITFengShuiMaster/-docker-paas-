package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.im.FeignTianyuIm;
import cn.edu.jit.tianyu_paas.ms.service.CustomerServiceStaffService;
import cn.edu.jit.tianyu_paas.shared.entity.CustomerServiceStaff;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author 汪继友
 * @since 2018-07-17
 */
@RestController
@RequestMapping("/customer-services")
public class CustomerServiceController {

    private final CustomerServiceStaffService customerServiceStaffService;
    private final HttpSession httpSession;
    private final FeignTianyuIm feignTianyuIm;

    @Autowired
    public CustomerServiceController(CustomerServiceStaffService customerServiceStaffService, HttpSession httpSession, FeignTianyuIm feignTianyuIm) {
        this.customerServiceStaffService = customerServiceStaffService;
        this.httpSession = httpSession;
        this.feignTianyuIm = feignTianyuIm;
    }

    @PostMapping
    public TResult createCustomerService(@Validated CustomerServiceStaff customerServiceStaff) {
        TResult tResult = feignTianyuIm.createCustomerService(customerServiceStaff.getNick(), customerServiceStaff.getPhone(), customerServiceStaff.getEmail(),
                customerServiceStaff.getPwd(), CustomerServiceStaff.TYPE_CUSTOMER_SERVICE, "");
        if (tResult.getCode().equals(TResultCode.SUCCESS.getCode())) {
            if (customerServiceStaffService.insert(customerServiceStaff)) {
                return TResult.success();
            }
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return tResult;
    }

    @GetMapping("list")
    public TResult listCustomerServices(Page<CustomerServiceStaff> page) {
        return TResult.success(customerServiceStaffService.selectPage(page));
    }

    @GetMapping("/{customerServiceId}")
    public TResult getCustomerService(@PathVariable("customerServiceId") String customerServiceId) {
        return TResult.success(customerServiceStaffService.selectById(customerServiceId));
    }

    @DeleteMapping("/{customerServiceId}")
    public TResult deleteCustomerService(@PathVariable("customerServiceId") String customerServiceId) {
        TResult result = feignTianyuIm.deleteUser(customerServiceId);
        if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
            if (customerServiceStaffService.deleteById(customerServiceId)) {
                return TResult.success();
            }
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return result;
    }

    @PutMapping
    public TResult updateCustomerService(@Validated CustomerServiceStaff customerServiceStaff) {
        TResult tResult = feignTianyuIm.updateCustomerService(customerServiceStaff.getNick(), customerServiceStaff.getPhone(), customerServiceStaff.getEmail(),
                customerServiceStaff.getPwd(), CustomerServiceStaff.TYPE_CUSTOMER_SERVICE, "");
        if (tResult.getCode().equals(TResultCode.SUCCESS.getCode())) {
            if (customerServiceStaffService.updateById(customerServiceStaff)) {
                return TResult.success();
            }
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return tResult;
    }
}
