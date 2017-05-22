package com.pkh.schedule.controller;


import com.pkh.schedule.service.HandOutService;
import com.pkh.schedule.service.RunOnceADayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/10.
 */
@Controller
public class QuartzController {
    private static Logger logger = LoggerFactory.getLogger(Logger.class);
    @Autowired
    private RunOnceADayService runOnceADayService;

    @Autowired
    private HandOutService handOutService;

    @RequestMapping(value = "handout")
    @ResponseBody
    public Map<String,Object> rightAway(String serviceFunction){
        Map<String,Object> map = new HashMap<String, Object>();
        try {

            Method method = handOutService.getClass().getMethod(serviceFunction,new Class[]{});
            method.invoke(handOutService,new Object[]{});
            map.put("errorCode",0);
            map.put("errorMessage","同步任务执行成功");
        } catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    @RequestMapping(value = "synchronizationBusiToPkec")
    @ResponseBody
    public Map<String,Object> synchronizationBusiToPkec(){
        Map<String,Object> map = new HashMap<String, Object>();
        try {



            Map<String,Object> result = runOnceADayService.busiMerToPkecMer();
            if (!"0".equals(result.get("merchantErrorCode").toString())) {
                map.put("errorCode",1);
                map.put("errorMessage",result.get("merchantErrorMessage"));
                return map;
            }
            if (!"0".equals(result.get("shopErrorCode").toString())) {
                map.put("errorCode",1);
                map.put("errorMessage",result.get("merchantErrorMessage").toString()+","+result.get("shopErrorMessage").toString());
                return map;
            }
            map.put("errorCode",0);
            map.put("errorMessage","同步任务执行成功");
            logger.debug("synchronizationBusiToPkec()....同步任务执行成功");
        } catch (Exception e) {
            map.put("errorCode",1);
            map.put("errorMessage","执行过程出现异常");
            logger.error("执行过程出现异常!!!");
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "doHisAccountBalanceAllLevelDaily")
    @ResponseBody
    public Map<String,Object> doHisAccountBalanceAllLevelDaily() {
        Map<String ,Object> map = new HashMap<String, Object>();
        try {
            map = runOnceADayService.doHisAccountBalanceAllLevelDaily();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }




//    @RequestMapping(value = "/updateQuartzTime")
//    @ResponseBody
//    public Map<String,Object> customerQuartz(String hour,String minute,ServletContextEvent event){
//        Map<String,Object> map = new HashMap<String, Object>();
//        try {
//            scheduler.start();
//            JobDetail job = JobBuilder.newJob().ofType(QuartzJob.class)
//                    .withIdentity("jobCustomer", "startQuertz").build();
//            Trigger trigger = TriggerBuilder
//                    .newTrigger()
//                    .withIdentity("triggerCustomer", "startQuertz")
//                    .startNow()
////                  .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(10, 2))
//                    .withSchedule(CronScheduleBuilder.cronSchedule("0 "+minute+" "+hour+" * * ?"))
////                  .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInHours(2))
//                    .build();
//            scheduler.scheduleJob(job, trigger);
//
////            scheduler.shutdown(true);
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//        QuartzContextListener quartzContextListener = new QuartzContextListener();
//
//        quartzContextListener.contextDestroyed(event);
//        quartzContextListener.contextInitialized(event);
//        return map;
//    }


    public void setHandOutService(HandOutService handOutService) {
        this.handOutService = handOutService;
    }

    public void setRunOnceADayService(RunOnceADayService runOnceADayService){
        this.runOnceADayService=runOnceADayService;
    }
}
