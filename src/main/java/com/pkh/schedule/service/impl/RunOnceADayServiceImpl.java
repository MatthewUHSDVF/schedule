package com.pkh.schedule.service.impl;

import com.pkh.pkcore.po.merchant.BusiMerchant;
import com.pkh.pkcore.po.merchant.BusiShop;
import com.pkh.pkcore.po.merchant.BusiShopCustom;
import com.pkh.pkec.product.dao.PKECMerchantMapper;
import com.pkh.pkec.product.dao.PKECShopMapper;
import com.pkh.pkec.product.po.PKECMerchant;
import com.pkh.pkec.product.po.PKECMerchantExample;
import com.pkh.pkec.product.po.PKECShop;
import com.pkh.pkec.product.po.PKECShopExample;
import com.pkh.schedule.dwr.SendMessage;
import com.pkh.schedule.service.RunOnceADayService;
import com.pkh.schedule.util.HttpClientUtils;
import com.pkh.schedule.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Administrator on 2016/11/3.
 */
public class RunOnceADayServiceImpl implements RunOnceADayService {

    private static Logger logger = LoggerFactory.getLogger(Logger.class);


    private PKECMerchantMapper pkecMerchantMapper;

    private PKECShopMapper pkecShopMapper;

    private String PKCORE;

    public RunOnceADayServiceImpl(String PKCORE ){
        this.PKCORE = PKCORE;
    }


    public Map<String,Object> busiMerToPkecMer() {
        Map<String,Object> result = new HashMap<String, Object>();
        Map<String,String> param = new HashMap<String, String>();
        PKECMerchant pkecMerchanttemp;
        PKECShop pkecShoptemp;
        List<PKECMerchant> updateMerchantList = new ArrayList<PKECMerchant>();
        List<PKECMerchant> insertMerchantList = new ArrayList<PKECMerchant>();
        List<PKECShop> updateShopList = new ArrayList<PKECShop>();
        List<PKECShop> insertShopList = new ArrayList<PKECShop>();
        int mcCountUpdateSuccess=0;
        int mcCountUpdateFail=0;
        int mcCountInsertSuccess=0;
        int mcCountInsertFail=0;
        int mcQueueFail=0;

        int soCountUpdateSuccess=0;
        int soCountUpdateFail=0;
        int soCountInsertSuccess=0;
        int soCountInsertFail=0;
        int soQueueFail=0;

        try {
            SendMessage.getMessage("开始执行");
            logger.info("正在将需要更新和添加的商户加入队列...");
            SendMessage.getMessage("正在将需要更新和添加的商户加入队列...");
            PKECMerchantExample pkecMerchantExample = new PKECMerchantExample();
            String jsonBusi = HttpClientUtils.doPost(PKCORE+"/merchantShop/queryAllMerchant",param);
            List<BusiMerchant> busiMerchantList = JsonUtils.json2list(jsonBusi, BusiMerchant.class);

//            pkecMerchantExample.createCriteria();
            List<PKECMerchant> pkecMerchantList = pkecMerchantMapper.selectByExample(pkecMerchantExample);
//            List<PKECMerchant> pkecMerchantList = pkecMerchantMapper.selectAllPKECMerchant();
            List<String> list = new ArrayList<String>();
            for (PKECMerchant pkecMerchant : pkecMerchantList) {
                list.add(pkecMerchant.getMcCode());
            }
            for (BusiMerchant busiMerchant : busiMerchantList) {
                try {
                    if (list.contains(busiMerchant.getMerchantCode())) {
                        for (PKECMerchant pkecMerchant : pkecMerchantList) {
                            if (pkecMerchant.getMcCode().equals(busiMerchant.getMerchantCode())) {
                                if (null != pkecMerchant.getMcUpdateTime() && null != busiMerchant.getMerchantUpdatetime()) {
                                    if (busiMerchant.getMerchantUpdatetime().after(pkecMerchant.getMcUpdateTime())) {
                                        pkecMerchanttemp = new PKECMerchant();
                                        pkecMerchanttemp.setMcId(pkecMerchant.getMcId());
                                        pkecMerchanttemp.setMcCode(busiMerchant.getMerchantCode());
                                        pkecMerchanttemp.setMcName(busiMerchant.getMerchantName());
                                        if (null != busiMerchant.getMerchantProvincecode()) {
                                            pkecMerchanttemp.setMcProvince(busiMerchant.getMerchantProvincecode());
                                        }
                                        if (null != busiMerchant.getMerchantCitycode() && !"".equals(busiMerchant.getMerchantCitycode())) {
                                            pkecMerchanttemp.setMcCity(Integer.parseInt(busiMerchant.getMerchantCitycode()));
                                        }
                                        if (null != busiMerchant.getMerchantDistrictcode() && !"".equals(busiMerchant.getMerchantDistrictcode())) {
                                            pkecMerchanttemp.setMcDistrict(Integer.parseInt(busiMerchant.getMerchantDistrictcode()));
                                        }
                                        pkecMerchanttemp.setMcUpdateTime(busiMerchant.getMerchantUpdatetime());
                                        updateMerchantList.add(pkecMerchanttemp);
                                        logger.info("updateMerchantList.add(pkecMerchanttemp)......"+pkecMerchanttemp.getMcCode()+"=》"+pkecMerchanttemp.getMcName());
                                        break;
                                    }
                                } else if (null == pkecMerchant.getMcUpdateTime() && null != busiMerchant.getMerchantUpdatetime()) {
                                    pkecMerchanttemp = new PKECMerchant();
                                    pkecMerchanttemp.setMcId(pkecMerchant.getMcId());
                                    pkecMerchanttemp.setMcCode(busiMerchant.getMerchantCode());
                                    pkecMerchanttemp.setMcName(busiMerchant.getMerchantName());
                                    if (null != busiMerchant.getMerchantProvincecode()) {
                                        pkecMerchanttemp.setMcProvince(busiMerchant.getMerchantProvincecode());
                                    }
                                    if (null != busiMerchant.getMerchantCitycode() && !"".equals(busiMerchant.getMerchantCitycode())) {
                                        pkecMerchanttemp.setMcCity(Integer.parseInt(busiMerchant.getMerchantCitycode()));
                                    }
                                    if (null != busiMerchant.getMerchantDistrictcode() && !"".equals(busiMerchant.getMerchantDistrictcode())) {
                                        pkecMerchanttemp.setMcDistrict(Integer.parseInt(busiMerchant.getMerchantDistrictcode()));
                                    }
                                    pkecMerchanttemp.setMcUpdateTime(busiMerchant.getMerchantUpdatetime());
                                    updateMerchantList.add(pkecMerchanttemp);
                                    logger.info("updateMerchantList.add(pkecMerchanttemp)......"+pkecMerchanttemp.getMcCode()+"=》"+pkecMerchanttemp.getMcName());
                                    break;
                                }

                            }
                        }
                    }else {
                        pkecMerchanttemp = new PKECMerchant();
                        pkecMerchanttemp.setMcCode(busiMerchant.getMerchantCode());
                        pkecMerchanttemp.setMcName(busiMerchant.getMerchantName());
                        if (null != busiMerchant.getMerchantProvincecode()) {
                            pkecMerchanttemp.setMcProvince(busiMerchant.getMerchantProvincecode());
                        }if (null != busiMerchant.getMerchantCitycode() && !"".equals(busiMerchant.getMerchantCitycode())) {
                            pkecMerchanttemp.setMcCity(Integer.parseInt(busiMerchant.getMerchantCitycode()));
                        }if (null != busiMerchant.getMerchantDistrictcode() && !"".equals(busiMerchant.getMerchantDistrictcode())) {
                            pkecMerchanttemp.setMcDistrict(Integer.parseInt(busiMerchant.getMerchantDistrictcode()));
                        }
//                        pkecMerchanttemp.setMcCreateTime(new Date());
                        if (null != busiMerchant.getMerchantUpdatetime()) {
                            pkecMerchanttemp.setMcUpdateTime(busiMerchant.getMerchantUpdatetime());
                        }else{
                            pkecMerchanttemp.setMcUpdateTime(new Date());
                        }
                        pkecMerchanttemp.setMcCreateTime(new Date());
                        insertMerchantList.add(pkecMerchanttemp);
                        logger.info("insertMerchantList.add(pkecMerchanttemp)"+pkecMerchanttemp.getMcCode()+"=》"+pkecMerchanttemp.getMcName());
                    }
                } catch (Exception e) {
                    logger.info("本条记录同步异常=>"+busiMerchant.getMerchantCode()+busiMerchant.getMerchantName());
                    mcQueueFail++;
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }

            }
            if (updateMerchantList.size()>0) {
                for (PKECMerchant pkecMerchant : updateMerchantList){
                    try {
                        pkecMerchantExample = new PKECMerchantExample();
                        pkecMerchantExample.createCriteria().andMcCodeEqualTo(pkecMerchant.getMcCode());
                        pkecMerchant.setMcId(null);
                        pkecMerchant.setMcCode(null);
                        pkecMerchantMapper.updateByExampleSelective(pkecMerchant,pkecMerchantExample);
                        mcCountUpdateSuccess++;
                        logger.info("商户更新成功+"+mcCountUpdateSuccess);
                    }catch (Exception e){
                        mcCountUpdateFail++;
                        logger.info("商户更新异常+"+mcCountUpdateFail);
                        logger.info("失败记录："+pkecMerchant.getMcCode()+"==>"+pkecMerchant.getMcName());
                        logger.info(e.getMessage());
                    }
                }
            }
            if (insertMerchantList.size()>0) {
                for (PKECMerchant pkecMerchant : insertMerchantList){
                    try {
                        pkecMerchantMapper.insertSelective(pkecMerchant);
                        mcCountInsertSuccess++;
                        logger.info("商户插入成功+"+mcCountInsertSuccess);
                    }catch (Exception e){
                        mcCountInsertFail++;
                        logger.info("商户插入异常+"+mcCountInsertFail);
                        logger.info("失败记录："+pkecMerchant.getMcCode()+"==>"+pkecMerchant.getMcName());
                        logger.info(e.getMessage());
                    }
                }
            }

            result.put("merchantErrorCode",0);
            result.put("merchantErrorMessage","商户同步成功");
            logger.info("商户同步完成");
            SendMessage.getMessage("商户同步完成");
        } catch (Exception e) {
            result.put("merchantErrorCode",1);
            result.put("merchantErrorMessage","商户同步异常");
            logger.info("商户同步异常");
            try {
                SendMessage.getMessage("商户同步异常");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        try {
            SendMessage.getMessage("正在将需要更新和添加的门店加入队列...");
            param = new HashMap<String, String>();
            String jsonBusi = HttpClientUtils.doPost(PKCORE + "/merchantShop/queryAllShop", param);
            List<BusiShopCustom> busiShopList = JsonUtils.json2list(jsonBusi, BusiShopCustom.class);
            PKECShopExample pkecShopExample = new PKECShopExample();
//            pkecShopExample.createCriteria();
            List<PKECShop> pkecShopList = pkecShopMapper.selectByExample(pkecShopExample);

            List<String> list = new ArrayList<String>();
            for (PKECShop pkecShop : pkecShopList) {
                list.add(pkecShop.getShopCode());
            }

            for (BusiShop busiShop : busiShopList) {
                try{
                    if (list.contains(busiShop.getShopCode())) {
                        for (PKECShop pkecShop : pkecShopList) {
                            if (pkecShop.getShopCode().equals(busiShop.getShopCode())) {
                                if(null!=pkecShop.getShopUpdateTime() && null != busiShop.getShopUpdatetime()){
                                    if (busiShop.getShopUpdatetime().after(pkecShop.getShopUpdateTime())) {
                                        pkecShoptemp = busiTotemp(busiShop);
                                        if(pkecShoptemp.getShopCode()==null){
                                            continue;
                                        }
                                        pkecShoptemp.setShopUpdateTime(new Date());
                                        updateShopList.add(pkecShoptemp);
                                        logger.info("updateShopList.add(pkecShoptemp)........"+pkecShoptemp.getShopCode()+"=>"+pkecShoptemp.getShopName());
                                        break;
                                    }else if (null==pkecShop.getShopUpdateTime() && null != busiShop.getShopUpdatetime()){
                                        pkecShoptemp = busiTotemp(busiShop);
                                        if(pkecShoptemp.getShopCode()==null){
                                            continue;
                                        }
                                        pkecShoptemp.setShopUpdateTime(new Date());
                                        updateShopList.add(pkecShoptemp);
                                        logger.info("updateShopList.add(pkecShoptemp)........"+pkecShoptemp.getShopCode()+"=>"+pkecShoptemp.getShopName());
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        pkecShoptemp = busiTotemp(busiShop);
                        if(pkecShoptemp.getShopCode()==null){
                            continue;
                        }
                        pkecShoptemp.setShopCreateTime(new Date());
                        if (null != busiShop.getShopUpdatetime()) {
                            pkecShoptemp.setShopUpdateTime(busiShop.getShopUpdatetime());
                        }else {
                            pkecShoptemp.setShopUpdateTime(new Date());
                        }
                        logger.info("updateShopList.add(pkecShoptemp)........"+pkecShoptemp.getShopCode()+"=>"+pkecShoptemp.getShopName());
                        insertShopList.add(pkecShoptemp);
                    }
                } catch (Exception e){
                    logger.info("本条记录同步异常=>门店编号为:"+busiShop.getShopCode()+";门店名:"+busiShop.getShopName());
                    logger.info("请检查信息输入是否合法......");
                    soQueueFail++;
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }

            }
            if (updateShopList.size()>0){
                for (PKECShop pkecShop : updateShopList) {
                    try {
                        pkecShopExample = new PKECShopExample();
                        pkecShopExample.createCriteria().andShopCodeEqualTo(pkecShop.getShopCode());
                        pkecShop.setShopId(null);
                        pkecShop.setShopCode(null);
                        pkecShopMapper.updateByExampleSelective(pkecShop, pkecShopExample);
                        soCountUpdateSuccess++;
                        logger.info("门店更新成功+"+soCountUpdateSuccess);
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                        soCountUpdateFail++;
                        logger.info("门店更新异常+"+soCountUpdateFail);
                    }
                }
            }
            if (insertShopList.size()>0) {
                for (PKECShop pkecShop : insertShopList) {
                    try {
                        pkecShopMapper.insert(pkecShop);
                        soCountInsertSuccess++;
                        logger.info("门店新增成功+"+soCountInsertSuccess);
                        SendMessage.getMessage("门店新增成功+"+soCountInsertSuccess);
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                        soCountInsertFail++;
                        logger.info("门店新增异常+"+soCountInsertFail);
                        logger.info("执行过程发生异常.....继续执行");
                        SendMessage.getMessage("门店新增异常+"+soCountInsertFail);
                        SendMessage.getMessage("执行过程发生异常.....继续执行");
                    }
                }
            }

            result.put("shopErrorCode",0);
            result.put("shopErrorMessage","门店同步成功");
            SendMessage.getMessage("门店同步成功");
        }catch (Exception e){
            result.put("shopErrorCode",1);
            result.put("shopErrorMessage","门店同步异常");
            logger.info(e.getMessage());
            logger.info("门店同步异常");
            try {
                SendMessage.getMessage("门店同步异常");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        logger.info("本次同步详情：");
        logger.info("商户：添加到队列成功："+(updateMerchantList.size()+insertMerchantList.size()));
        logger.info("商户：添加到队列失败："+mcQueueFail);
        logger.info("商户：队列入库更新成功："+mcCountUpdateSuccess);
        logger.info("商户：队列入库更新失败："+mcCountUpdateFail);
        logger.info("商户：队列入库新增成功："+mcCountInsertSuccess);
        logger.info("商户：队列入库新增失败："+mcCountInsertFail);
        logger.info("门店：添加到队列成功："+(updateShopList.size()+insertShopList.size()));
        logger.info("门店：添加到队列失败："+soQueueFail);
        logger.info("门店：队列入库更新成功："+soCountUpdateSuccess);
        logger.info("门店：队列入库更新失败："+soCountUpdateFail);
        logger.info("门店：队列入库新增成功："+soCountInsertSuccess);
        logger.info("门店：队列入库新增失败："+soCountInsertFail);
        try {
            SendMessage.getMessage("本次同步详情：");
            SendMessage.getMessage("商户：添加到队列成功："+(updateMerchantList.size()+insertMerchantList.size()));
            SendMessage.getMessage("商户：添加到队列失败："+mcQueueFail);
            SendMessage.getMessage("商户：队列入库更新成功："+mcCountUpdateSuccess);
            SendMessage.getMessage("商户：队列入库更新失败："+mcCountUpdateFail);
            SendMessage.getMessage("商户：队列入库新增成功："+mcCountInsertSuccess);
            SendMessage.getMessage("商户：队列入库新增失败："+mcCountInsertFail);
            SendMessage.getMessage("门店：添加到队列成功："+(updateShopList.size()+insertShopList.size()));
            SendMessage.getMessage("门店：添加到队列失败："+soQueueFail);
            SendMessage.getMessage("门店：队列入库更新成功："+soCountUpdateSuccess);
            SendMessage.getMessage("门店：队列入库更新失败："+soCountUpdateFail);
            SendMessage.getMessage("门店：队列入库新增成功："+soCountInsertSuccess);
            SendMessage.getMessage("门店：队列入库新增失败："+soCountInsertFail);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private PKECShop busiTotemp(BusiShop busiShop){
        PKECShop pkecShoptemp = new PKECShop();
        if (null==busiShop.getShopMerchantcode() || "".equals(busiShop.getShopMerchantcode())) {
            return pkecShoptemp;
        }
        PKECMerchantExample pkecMerchantExample = new PKECMerchantExample();
        pkecMerchantExample.createCriteria().andMcCodeEqualTo(busiShop.getShopMerchantcode());
        List<PKECMerchant> merchantList = pkecMerchantMapper.selectByExample(pkecMerchantExample);
        if (merchantList.size() > 0) {
            busiShop.getShopMerchantcode();
            pkecShoptemp.setShopCode(busiShop.getShopCode());
            pkecShoptemp.setShopName(busiShop.getShopName());
            pkecShoptemp.setShopMerchant(merchantList.get(0).getMcId());

            if (null != busiShop.getShopProvincecode()) {
                pkecShoptemp.setShopProvince(busiShop.getShopProvincecode());
            }
            if (null != busiShop.getShopCitycode() || !"".equals(busiShop.getShopCitycode())) {
                try {
                    pkecShoptemp.setShopCity(Integer.parseInt(busiShop.getShopCitycode()));
                }catch (Exception e){
                    logger.info(e.getMessage());
                }

            }
             if (null != busiShop.getShopDistrictcode() && !("".equals(busiShop.getShopDistrictcode()))) {
                 try {
                     pkecShoptemp.setShopDistinct(Integer.parseInt(busiShop.getShopDistrictcode()));
                 }catch (Exception e){
                     logger.info(e.getMessage());
                 }
            }
            if (null != busiShop.getShopAddress() || !"".equals(busiShop.getShopAddress())) {
                pkecShoptemp.setShopAddress(busiShop.getShopAddress());
            }
            if (null != busiShop.getShopLongitude() ) {
                pkecShoptemp.setShopLongitude(busiShop.getShopLongitude().doubleValue());
            }
            if (null != busiShop.getShopLatitude()) {
                pkecShoptemp.setShopLatitude(busiShop.getShopLatitude().doubleValue());
            }
            if (null != busiShop.getShopPhone() || !"".equals(busiShop.getShopPhone())) {
                pkecShoptemp.setShopTel(busiShop.getShopPhone());
            }
            if (null != busiShop.getShopType()) {
                pkecShoptemp.setShopType(busiShop.getShopType());
            }
        }
        return  pkecShoptemp;
    }


    public Map<String,Object> doHisAccountBalanceAllLevelDaily(){
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,String> param = new HashMap<String, String>();
        String jsonMap = HttpClientUtils.doPost(PKCORE+"/audit/doHisAccountBalanceAllLevelDaily",param);
        try {
            map = JsonUtils.json2map(jsonMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public void setPkecMerchantMapper(PKECMerchantMapper pkecMerchantMapper) {
        this.pkecMerchantMapper = pkecMerchantMapper;
    }

    public void setPkecShopMapper(PKECShopMapper pkecShopMapper) {
        this.pkecShopMapper = pkecShopMapper;
    }
}
