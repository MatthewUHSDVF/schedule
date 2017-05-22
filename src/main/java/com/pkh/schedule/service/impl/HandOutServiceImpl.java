package com.pkh.schedule.service.impl;

import com.pkh.schedule.po.SysScheduler;
import com.pkh.schedule.service.CustomerQuartzTimeService;
import com.pkh.schedule.service.HandOutService;
import com.pkh.schedule.service.RunOnceADayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/25.
 */
@Service
public class HandOutServiceImpl implements HandOutService {

    @Autowired
    private RunOnceADayService runOnceADayService;

    @Override
    public Map<String, Object> busiMerToPkecMer() {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            map =runOnceADayService.busiMerToPkecMer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public void setRunOnceADayService(RunOnceADayService runOnceADayService) {
        this.runOnceADayService = runOnceADayService;
    }
}
