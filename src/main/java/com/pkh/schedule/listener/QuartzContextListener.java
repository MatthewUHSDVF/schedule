package com.pkh.schedule.listener;

import org.quartz.Scheduler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Administrator on 2016/11/21.
 */
public class QuartzContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try{
            Scheduler startQuartz = (Scheduler) WebApplicationContextUtils
                    .getWebApplicationContext(event.getServletContext())
                    .getBean("startQuertz");

            if(startQuartz != null){
                startQuartz.shutdown(true);
            }

            Thread.sleep(1000);//主线程睡眠1s
        }catch (Exception e){
            e.printStackTrace();
        }
        event.getServletContext().log("QuartzContextListener销毁成功！");
        System.out.println("QuartzContextListener销毁成功！");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("QuartzContextListener启动成功！");
        event.getServletContext().log("QuartzContextListener启动成功！");
    }
}
