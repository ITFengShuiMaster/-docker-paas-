package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.im.FeignTianyuIm;
import cn.edu.jit.tianyu_paas.ms.service.CustomerServiceStaffService;
import cn.edu.jit.tianyu_paas.shared.entity.CustomerServiceStaff;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

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

    @ApiOperation("增加客服人员")
    @PostMapping
    public TResult createCustomerService(@Validated CustomerServiceStaff customerServiceStaff) {
        TResult tResult = feignTianyuIm.createCustomerService(customerServiceStaff.getNick(), customerServiceStaff.getPhone(), customerServiceStaff.getEmail(),
                customerServiceStaff.getPwd(), User.TYPE_CUSTOMER_SERVICE, "");
        if (tResult.getCode().equals(TResultCode.SUCCESS.getCode())) {
            customerServiceStaff.setGmtCreate(new Date());
            customerServiceStaff.setGmtModified(new Date());
            if (customerServiceStaffService.insert(customerServiceStaff)) {
                return TResult.success();
            }
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return tResult;
    }

    @ApiOperation("获取客服人员列表")
    @GetMapping("list")
    public TResult listCustomerServices(Page<CustomerServiceStaff> page) {
        return TResult.success(customerServiceStaffService.selectPage(page));
    }

    @ApiOperation("查询某个客服人员")
    @GetMapping("/{customerServiceId}")
    public TResult getCustomerService(@PathVariable("customerServiceId") String customerServiceId) {
        return TResult.success(customerServiceStaffService.selectById(customerServiceId));
    }

    @ApiOperation("删除某个客服人员")
    @DeleteMapping("/{customerServiceId}")
    public TResult deleteCustomerService(@PathVariable("customerServiceId") long customerServiceId) {
        TResult result = feignTianyuIm.deleteUser(customerServiceId);
        if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
            if (customerServiceStaffService.deleteById(customerServiceId)) {
                return TResult.success();
            }
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return result;
    }

    @ApiOperation("更新客服人员信息")
    @PutMapping
    public TResult updateCustomerService(@Validated CustomerServiceStaff customerServiceStaff) {
        TResult tResult = feignTianyuIm.updateCustomerService(customerServiceStaff.getCustomerServiceStaffId(), customerServiceStaff.getNick(), customerServiceStaff.getPhone(),
                customerServiceStaff.getEmail(), customerServiceStaff.getPwd(), User.TYPE_CUSTOMER_SERVICE, "");
        if (tResult.getCode().equals(TResultCode.SUCCESS.getCode())) {
            customerServiceStaff.setGmtModified(new Date());
            if (customerServiceStaffService.updateById(customerServiceStaff)) {
                return TResult.success();
            }
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return tResult;
    }
}
