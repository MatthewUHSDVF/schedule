package com.pkh.schedule.service.impl;

import com.pkh.schedule.service.RunOnceADayService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/3.
 */
public class QuartzJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private RunOnceADayService runOnceADayService;

    public void work() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =  new Date();
        logger.debug(sdf.format(date) + "  执行Quartz定时器");
        System.out.println(sdf.format(date) + "  执行Quartz定时器");
        runOnceADayService.busiMerToPkecMer();

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =  new Date();
        logger.debug(sdf.format(date) + "  执行Quartz定时器");
        System.out.println(sdf.format(date) + "  执行Quartz定时器");
    }
}
