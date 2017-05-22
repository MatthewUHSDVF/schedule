package com.pkh.schedule.service.impl;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Administrator on 2016/11/24.
 */
public class InitCronTrigger implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(Logger.class);

    public static Scheduler scheduler = null;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        logger.info("The application start...");

        /* 注册定时任务 */
        try {
            // 获取Scheduler实例
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // 具体任务
            JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity("job1", "group1").build();

            // 触发时间点
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5).repeatForever();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                    .startNow().withSchedule(simpleScheduleBuilder).build();

            // 交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);

            logger.info("The scheduler register...");
        } catch (SchedulerException se) {
            logger.error(se.getMessage(), se);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("The application stop...");

        /* 注销定时任务 */
        try {
            // 关闭Scheduler
            scheduler.shutdown();

            logger.info("The scheduler shutdown...");
        } catch (SchedulerException se) {
            logger.error(se.getMessage(), se);
        }
    }
}
