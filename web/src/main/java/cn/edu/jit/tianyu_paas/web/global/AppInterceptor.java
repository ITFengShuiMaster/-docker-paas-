package cn.edu.jit.tianyu_paas.web.global;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import cn.edu.jit.tianyu_paas.web.util.SpringBeanFactoryUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * app controller的拦截器，保证用户访问的app，是自己的app
 *
 * @author 天宇小凡
 */
public class AppInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(AppInterceptor.class);
    private AppService appService = SpringBeanFactoryUtil.getBean(AppService.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        String requestURI = httpServletRequest.getRequestURI();
        logger.debug(requestURI);

        HttpSession session = httpServletRequest.getSession();
        long userId = (long) session.getAttribute(Constants.SESSION_KEY_USER_ID);

        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        String appId = parameterMap.get("appId")[0];

        // 根据userid和appid取出对应记录，如果记录小于等于0，则拦截
        int count = appService.selectCount(new EntityWrapper<App>().eq("user_id", userId).eq("app_id", appId));
        if (count > 0) {
            return true;
        }
        logger.warn("异常请求：userId:" + userId + ", appId:" + appId);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
