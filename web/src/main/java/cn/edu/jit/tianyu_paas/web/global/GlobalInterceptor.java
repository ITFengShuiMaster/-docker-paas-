package cn.edu.jit.tianyu_paas.web.global;

import cn.edu.jit.tianyu_paas.shared.entity.Ticket;
import cn.edu.jit.tianyu_paas.web.service.TicketService;
import cn.edu.jit.tianyu_paas.web.util.SpringBeanFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class GlobalInterceptor implements HandlerInterceptor {

    @Value("${web.runtime}")
    private boolean runtime;
    @Value("${web.test-user-id}")
    private long testUserId;

    private Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);
    private TicketService ticketService = SpringBeanFactoryUtil.getBean(TicketService.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        logger.debug(httpServletRequest.getRequestURI());
        HttpSession session = httpServletRequest.getSession();

        // 调试模式
        if (!runtime) {
            logger.debug("debug mode test user: " + testUserId);
            session.setAttribute(Constants.SESSION_KEY_USER_ID, testUserId);
            return true;
        }

        if (session != null && session.getAttribute(Constants.SESSION_KEY_USER_ID) != null) {
            return true;
        }

        String token = httpServletRequest.getHeader(Constants.HEAD_TOKENE);
        Ticket ticket = ticketService.getTicketByToken(token);

        // token过期
        if (ticket == null || System.currentTimeMillis() - ticket.getGmtModified().getTime() > Constants.TOKEN_MAX_VALID) {
            return false;
        }

        // 刷新token
        ticket.setGmtModified(new Date());
        ticketService.updateById(ticket);
        session.setAttribute(Constants.SESSION_KEY_USER_ID, ticket.getUserId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}