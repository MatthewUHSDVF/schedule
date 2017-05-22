package com.pkh.schedule.service;

import com.pkh.schedule.po.SysScheduler;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24.
 */
public interface CustomerQuartzTimeService {
    void reScheduleJob();
    List<SysScheduler> getSysSchedulerFromDB();
    Map<String,Object> getAllSysSchedulerFromDB();
}
