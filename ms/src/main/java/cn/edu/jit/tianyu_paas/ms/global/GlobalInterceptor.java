package cn.edu.jit.tianyu_paas.ms.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GlobalInterceptor implements HandlerInterceptor {

    @Value("${ms.runtime}")
    private boolean runtime;
    @Value("${ms.test-user-id}")
    private long testUserId;

    private Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        logger.warn(httpServletRequest.getRequestURI());
        HttpSession session = httpServletRequest.getSession();
        if (!runtime) {
            logger.debug("debug mode test user: " + testUserId);
            session.setAttribute(Constants.SESSION_KEY_ADMIN_ID, testUserId);
            return true;
        }
        if (session == null || session.getAttribute(Constants.SESSION_KEY_ADMIN_ID) == null) {
            logger.warn("session null");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
