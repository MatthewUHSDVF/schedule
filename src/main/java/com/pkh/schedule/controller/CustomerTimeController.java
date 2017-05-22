package com.pkh.schedule.controller;

import com.pkh.schedule.po.SysScheduler;
import com.pkh.schedule.service.CustomerQuartzTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24.
 */
@Controller
public class CustomerTimeController {
    private static Logger logger = LoggerFactory.getLogger(Logger.class);
    @Autowired
    private CustomerQuartzTimeService customerQuartzTimeService;

    @RequestMapping(value = "checkScheduleJob")
    @ResponseBody
    public Map<String ,Object> checkScheduleJob(){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            customerQuartzTimeService.reScheduleJob();
            map.put("errorCode",0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("errorCode",1);
            map.put("errorMessage","执行过程出现异常");
        }
        return map;
    }

    @RequestMapping(value = "queryScheduler")
    @ResponseBody
    public Map<String , Object> selectSchedulerList(){
        Map<String , Object> map = new HashMap<String, Object>();
        try{
            List<SysScheduler> sysSchedulers = customerQuartzTimeService.getSysSchedulerFromDB();
            map.put("errorCode",0);
            map.put("sysSchedulers",sysSchedulers);
        }catch (Exception e){
            e.printStackTrace();
            map.put("errorCode",1);
            map.put("errorMessage","执行过程出现异常");
        }
        map.put("errorCode",0);
        map.put("errorMessage","执行chenggong");
        return map;
    }

    @RequestMapping(value = "queryAllScheduler")
    @ResponseBody
    public Map<String , Object> selectAllSchedulerList(){
        Map<String , Object> map = new HashMap<String, Object>();
        try{
            map = customerQuartzTimeService.getAllSysSchedulerFromDB();
            map.put("errorCode",0);
        }catch (Exception e){
            e.printStackTrace();
            map.put("errorCode",1);
            map.put("errorMessage","执行过程出现异常");
        }
        map.put("errorCode",0);
        map.put("errorMessage","执行chenggong");
        return map;
    }

    public void setCustomerQuartzTimeService(CustomerQuartzTimeService customerQuartzTimeService) {
        this.customerQuartzTimeService = customerQuartzTimeService;
    }
}
