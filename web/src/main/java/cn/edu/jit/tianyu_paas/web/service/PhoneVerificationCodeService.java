package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.PhoneVerificationCode;
import cn.edu.jit.tianyu_paas.web.mapper.PhoneVerificationCodeMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PhoneVerificationCodeService extends ServiceImpl<PhoneVerificationCodeMapper, PhoneVerificationCode> {
}
