package com.pkh.schedule.service.impl;

import com.pkh.schedule.mapper.SysSchedulerMapper;
import com.pkh.schedule.po.SysScheduler;
import com.pkh.schedule.po.SysSchedulerExample;
import com.pkh.schedule.service.CustomerQuartzTimeService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24.
 */
@Service
public class CustomerQuartzTimeServiceImpl implements CustomerQuartzTimeService {
    public void setSysSchedulerMapper(SysSchedulerMapper sysSchedulerMapper) {
        this.sysSchedulerMapper = sysSchedulerMapper;
    }

    @Autowired
    private SysSchedulerMapper sysSchedulerMapper;

    @Autowired
    private Scheduler scheduler;
    @PostConstruct
    public void reScheduleJob() {
        try {
            List<SysScheduler> sysSchedulerList = getSysSchedulerFromDB();
            for (SysScheduler sysScheduler : sysSchedulerList){
                if ("0".equals(sysScheduler.getIsExecute())) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(sysScheduler.getJobTrigger(), Scheduler.DEFAULT_GROUP);
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(sysScheduler.getJobTrigger(), Scheduler.DEFAULT_GROUP).withSchedule(CronScheduleBuilder.cronSchedule(sysScheduler.getCron())).build();
                    scheduler.rescheduleJob(triggerKey,trigger);
                }
            }

            // 下面是具体的job内容，可自行设置
            // executeJobDetail();
        } catch (SchedulerException se){
            se.printStackTrace();
        } catch (ParseException pe){
            pe.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<SysScheduler> getSysSchedulerFromDB(){

        SysSchedulerExample sysSchedulerExample = new SysSchedulerExample();
        sysSchedulerExample.createCriteria();
        List<SysScheduler> sysSchedulers = sysSchedulerMapper.selectByExample(sysSchedulerExample);
        List<SysScheduler> sysSchedulerstemp = new ArrayList<SysScheduler>();
        for (SysScheduler sysScheduler : sysSchedulers) {
            if ("0".equals(sysScheduler.getIsExecute())) {
                sysSchedulerstemp.add(sysScheduler);
            }
        }
        return sysSchedulerstemp;
    }

    public Map<String,Object> getAllSysSchedulerFromDB(){
        Map<String,Object> map = new HashMap<String, Object>();
        SysSchedulerExample sysSchedulerExample = new SysSchedulerExample();
        sysSchedulerExample.createCriteria();
        List<SysScheduler> sysSchedulers = sysSchedulerMapper.selectByExample(sysSchedulerExample);
        List<SysScheduler> invokeSchedulers = new ArrayList<SysScheduler>();
        List<SysScheduler> disabledSchedulers = new ArrayList<SysScheduler>();
        for (SysScheduler sysScheduler : sysSchedulers) {
            if ("0".equals(sysScheduler.getIsExecute())) {
                invokeSchedulers.add(sysScheduler);
            } else if ("1".equals(sysScheduler.getIsExecute())) {
                disabledSchedulers.add(sysScheduler);
            }
        }
        map.put("invokeSchedulers",invokeSchedulers);
        map.put("disabledSchedulers",disabledSchedulers);
        return map;
    }


    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
