package cn.stylefeng.guns.modular.finance.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.base.auth.context.LoginContextHolder;
import cn.stylefeng.guns.base.auth.model.LoginUser;
import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import cn.stylefeng.guns.modular.finance.service.StatisticsService;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;


    @RequestMapping("/info")
    public Object info() {
        return "/billing/statisticsInfo.html";
    }

    @RequestMapping("/pie")
    public Object pie() {
        return "/billing/statisticsPie.html";
    }

    @RequestMapping("/getMasterMap")
    @ResponseBody
    public Object getMasterMap(String dateStr) {
        Map<String, Object> data = new HashMap<>();
        Date date;
        if (ToolUtil.isEmpty(dateStr)) {
            date = new Date();
        } else {
            DateTime parse = DateUtil.parse(dateStr + "0101");
            date = parse.toJdkDate();
        }
        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();

        List<String> reports = statisticsService.getReportDate(date, user.getId());

        List<BillingReport> otherData = statisticsService.getReportData(date, "'income'", user.getId());
        otherData.forEach(c -> {
            c.setBillingAmount(c.getBillingAmount().negate());
        });
        List<BillingReport> incomeData = statisticsService.getReportData(date, "'repast','traffic','invest','repayment','online_shopping','shopping'", user.getId());
        //组织时间数据
        List<String> incomeDataList = incomeData.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<String> otherDateList = otherData.stream().map(c -> c.getDate()).collect(Collectors.toList());

        //利润集合
        List<BillingReport> profitList = new ArrayList<>();

        //数据补齐
        for (int i = 0; i < reports.size(); i++) {
            String b = reports.get(i);
            BillingReport billingReport = new BillingReport();
            billingReport.setBillingAmount(BigDecimal.ZERO);
            billingReport.setDate(b);
            billingReport.setTime(new Date());
            long count1 = incomeDataList.stream().filter(c -> c.equals(b)).count();//收入
            long count2 = otherDateList.stream().filter(c -> c.equals(b)).count();//支出
            //收入支出都为空 则利润为空
            if (count1 <= 0 && count2 <= 0) {
                profitList.add(billingReport);
                incomeData.add(billingReport);
                otherData.add(billingReport);
            }
            //收入为空  支出不为空 则利润为0
            if (count1 <= 0 && count2 > 0) {
                BillingReport billingReport1 = new BillingReport();
                billingReport1.setBillingAmount(otherData.get(i).getBillingAmount());
                billingReport1.setDate(b);
                profitList.add(billingReport);
                incomeData.add(billingReport);
            }
            //收入不为空  支出为空 则利润为收入
            if (count1 > 0 && count2 <= 0) {
                BillingReport billingReport1 = new BillingReport();
                billingReport1.setBillingAmount(incomeData.get(i).getBillingAmount());
                billingReport1.setDate(b);
                profitList.add(billingReport1);
                otherData.add(billingReport);
            }
            //收入不为空  支出不为空 则利润为收入+支出
            if (count1 > 0 && count2 > 0) {
                BillingReport billingReport1 = new BillingReport();
                BigDecimal subtract = incomeData.get(i).getBillingAmount().add(otherData.get(i).getBillingAmount());
                billingReport1.setBillingAmount(subtract);
                billingReport1.setDate(b);
                profitList.add(billingReport1);
            }
        }

        List<String> incomeCollect = incomeData.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c -> c.getBillingAmount().toString()).collect(Collectors.toList());
        List<String> otherCollect = otherData.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c -> c.getBillingAmount().toString()).collect(Collectors.toList());
        List<String> profitCollect = profitList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c -> c.getBillingAmount().toString()).collect(Collectors.toList());

        data.put("dateList", reports);
        data.put("incomeCollect", incomeCollect);
        data.put("otherCollect", otherCollect);
        data.put("profitCollect", profitCollect);
        return ResponseData.success(0000, "", data);
    }

    @RequestMapping("/getPieMap")
    @ResponseBody
    public Object getPieMap(String dateStr) {
        Map<String, Object> data = new HashMap<>();
        Date date;
        if (ToolUtil.isEmpty(dateStr)) {
            date = new Date();
        } else {
            DateTime parse = DateUtil.parse(dateStr + "0101");
            date = parse.toJdkDate();
        }
        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Map<String, Object>> dataList = statisticsService.getPieMapData(date, user.getId());
        List<Map<String, Object>> responsDataList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        dataList.forEach(c -> {
            Map<String, Object> map = new HashMap<>();
            if (c.get("name").toString().equals("repast")) {
                nameList.add("餐饮");
                map.put("value", c.get("value"));
                map.put("name", "餐饮");
                responsDataList.add(map);
            }
            if (c.get("name").toString().equals("traffic")) {
                nameList.add("交通");
                map.put("value", c.get("value"));
                map.put("name", "交通");
                responsDataList.add(map);
            }
            if (c.get("name").toString().equals("invest")) {
                nameList.add("投资");
                map.put("value", c.get("value"));
                map.put("name", "投资");
                responsDataList.add(map);
            }
            if (c.get("name").toString().equals("repayment")) {
                nameList.add("还款");
                map.put("value", c.get("value"));
                map.put("name", "还款");
                responsDataList.add(map);
            }
            if (c.get("name").toString().equals("online_shopping")) {
                nameList.add("线上消费");
                map.put("value", c.get("value"));
                map.put("name", "线上消费");
                responsDataList.add(map);
            }
            if (c.get("name").toString().equals("shopping")) {
                nameList.add("线下消费");
                map.put("value", c.get("value"));
                map.put("name", "线下消费");
                responsDataList.add(map);
            }
        });
        data.put("dataList", responsDataList);
        data.put("nameList", nameList);
        return ResponseData.success(0000, "", data);
    }


}
