package cn.edu.jit.tianyu_paas.im.mina;

import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 全局session管理器
 * 所有会话全部存放在内存中，可持久化到redis
 *
 * @author 天宇小凡
 */
public class GlobalSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalSession.class);
    /**
     * 存放所有客服的session和对应的用户，按照分配用户数降序存储
     */
    private static List<CustomerServiceAndUsers> onlineCustomerServices = Collections.synchronizedList(new ArrayList<>());
    /**
     * 暂未分配到客服的用户（当前没有客服在线）
     */
    private static List<IoSession> singleUserSessionList = Collections.synchronizedList(new ArrayList<>());

    private static ConcurrentMap<Long, IoSession> userSessionMap = new ConcurrentHashMap<>();

    /**
     * 用户登录，分配客服
     *
     * @param userSession 用户session
     */
    public static void userLogin(IoSession userSession, Long userId) {
        userSessionMap.put(userId, userSession);
        // 如果没有客服在线，则放到未分配列表中
        if (onlineCustomerServices.size() <= 0) {
            singleUserSessionList.add(userSession);
            return;
        }
        // 将用户分配给最空闲的客服
        IoSession mostIdleCustomerServiceSession = onlineCustomerServices.get(onlineCustomerServices.size() - 1).customerServiceSession;
        onlineCustomerServices.get(onlineCustomerServices.size() - 1).userSessions.add(userSession);
        // 设置用户对应的客服
        userSession.setAttribute(MinaConstant.SESSION_KEY_CUSTOMER_SERVICE, mostIdleCustomerServiceSession);

        // 对list排序，让最空闲客服在最后
        sortList();
    }

    /**
     * 用户离线，释放客服
     */
    public static void userLogout(IoSession userSession, Long userId) {
        userSessionMap.remove(userId);
        // 得到对应客服
        IoSession correspondingCustomerService = (IoSession) userSession.getAttribute(MinaConstant.SESSION_KEY_CUSTOMER_SERVICE);
        userSession.removeAttribute(MinaConstant.SESSION_KEY_CUSTOMER_SERVICE);
        // 对应客服负责的用户中删除离线用户
        List<IoSession> userSessions = getCustomerServiceUserSessions(correspondingCustomerService);
        userSessions.remove(userSession);
    }

    /**
     * 客服登录，分配用户（指没有分配到客服的用户）
     */
    public static void customerServiceLogin(IoSession customerServiceSession) {
        CustomerServiceAndUsers customerServiceAndUsers = new CustomerServiceAndUsers();
        customerServiceAndUsers.customerServiceSession = customerServiceSession;
        // 将全部未分配用户，分配给新上线的客服（因为是唯一客服）
        customerServiceAndUsers.userSessions.addAll(singleUserSessionList);
        for (IoSession userSession : singleUserSessionList) {
            userSession.setAttribute(MinaConstant.SESSION_KEY_CUSTOMER_SERVICE, customerServiceSession);
        }
        onlineCustomerServices.add(customerServiceAndUsers);
        // 清除未分配用户
        singleUserSessionList.clear();
        // 排序
        sortList();
    }

    /**
     * 客服离线，释放用户，并分配给其他客服
     */
    public static void customerServiceLogout(IoSession customerServiceSession) {
        // 客服离线后，对应的剩下的用户
        List<IoSession> leftUserSessions = getCustomerServiceUserSessions(customerServiceSession);
        for (CustomerServiceAndUsers customerServiceAndUsers : onlineCustomerServices) {
            if (customerServiceAndUsers.customerServiceSession.equals(customerServiceSession)) {
                onlineCustomerServices.remove(customerServiceAndUsers);
                break;
            }
        }
        // 如果没有客服在线
        if (onlineCustomerServices.size() <= 0) {
            singleUserSessionList.addAll(leftUserSessions);
        } else {
            // 将用户分配给最空闲的客服
            IoSession mostIdleCustomerServiceSession = onlineCustomerServices.get(onlineCustomerServices.size() - 1).customerServiceSession;
            onlineCustomerServices.get(onlineCustomerServices.size() - 1).userSessions.addAll(leftUserSessions);
            for (IoSession leftUserSession : leftUserSessions) {
                leftUserSession.setAttribute(MinaConstant.SESSION_KEY_CUSTOMER_SERVICE, mostIdleCustomerServiceSession);
            }
        }
    }

    /**
     * 用户发送了消息
     */
    public static void userSendMessage(IoSession userSession, Object message) {
        // TODO 标记发送者
        IoSession correspondingCustomerServiceSession = (IoSession) userSession.getAttribute(MinaConstant.SESSION_KEY_CUSTOMER_SERVICE);
        correspondingCustomerServiceSession.write(message);
    }

    /**
     * 客服发送给用户消息
     */
    public static void customerServiceSendMessage(IoSession customerServiceSession, Long receiver, Object message) {
        // TODO 标记发送者
        IoSession userSession = userSessionMap.get(receiver);
        userSession.write(message);
    }

    /**
     * 根据每个客服负责的用户数进行降序排序 ，保证最后一个是最空闲客服
     */
    private static void sortList() {
        onlineCustomerServices.sort((o1, o2) -> o2.userSessions.size() - o1.userSessions.size());
    }

    /**
     * 得到每一个客服负责的用户列表
     *
     * @param correspondingCustomerServiceSession 客服session
     */
    private static List<IoSession> getCustomerServiceUserSessions(IoSession correspondingCustomerServiceSession) {
        for (CustomerServiceAndUsers customerServiceAndUsers : onlineCustomerServices) {
            if (customerServiceAndUsers.customerServiceSession.equals(correspondingCustomerServiceSession)) {
                return customerServiceAndUsers.userSessions;
            }
        }
        LOGGER.error("null null null");
        return new ArrayList<>();
    }

    static class CustomerServiceAndUsers {
        IoSession customerServiceSession;
        List<IoSession> userSessions;
    }
}