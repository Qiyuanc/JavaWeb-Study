package com.qiyuan.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @ClassName OnlineCountListener
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/3 15:25
 * @Version 1.0
 **/
//统计网站在线人数：统计Session，将数据保存在ServletContext中
public class OnlineCountListener implements HttpSessionListener {
    //服务器启动时会产生Session，需要Redeploy重新部署才能正常
    //Session创建监听，Session创建就会触发
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println(httpSessionEvent.getSession().getId());
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        Integer onlineCount = (Integer) context.getAttribute("OnlineCount");
        if (onlineCount == null) {
            onlineCount = new Integer(1);
        } else {
            int count = onlineCount.intValue();
            onlineCount = new Integer(count + 1);
        }
        context.setAttribute("OnlineCount", onlineCount);
    }
    //Session销毁监听，Session销毁就会触发
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        System.out.println("销毁");
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        Integer onlineCount = (Integer) context.getAttribute("OnlineCount");
        int count = onlineCount.intValue();
        onlineCount = new Integer(count - 1);
        context.setAttribute("OnlineCount", onlineCount);
    }
    /*Session销毁方式
    1.手动销毁 httpSessionEvent.getSession().invalidate();
    2.自动销毁 在web.xml中配置session-config，设置session存活时间
    * */
}
