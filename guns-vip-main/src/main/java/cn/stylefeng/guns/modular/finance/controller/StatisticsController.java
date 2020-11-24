package cn.stylefeng.guns.modular.finance.controller;

import cn.stylefeng.guns.base.auth.context.LoginContextHolder;
import cn.stylefeng.guns.base.auth.model.LoginUser;
import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import cn.stylefeng.guns.modular.finance.service.StatisticsService;
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


    @RequestMapping("/getMasterMap")
    @ResponseBody
    public Object getMasterMap() {
        Map<String, Object> data = new HashMap<>();
        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();

        List<String> reports = statisticsService.getReportDate(new Date(), user.getId());

        List<BillingReport> otherData = statisticsService.getReportData(new Date(), "'income'", user.getId());
        otherData.forEach(c -> {
            c.setBillingAmount(c.getBillingAmount().negate());
        });
        List<BillingReport> incomeData = statisticsService.getReportData(new Date(), "'repast','traffic','invest','repayment','online_shopping','shopping'", user.getId());

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

            if (count1 <= 0 && count2 <= 0) {
                profitList.add(billingReport);
                incomeData.add(billingReport);
                otherData.add(billingReport);
            }
            if (count1 <= 0 && count2 > 0) {
                BillingReport billingReport1 = new BillingReport();
                billingReport1.setBillingAmount(otherData.get(i).getBillingAmount().negate());
                billingReport1.setDate(b);
                profitList.add(billingReport1);
                incomeData.add(billingReport);
            }
            if (count1 > 0 && count2 <= 0) {
                BillingReport billingReport1 = new BillingReport();
                billingReport1.setBillingAmount(incomeData.get(i).getBillingAmount());
                billingReport1.setDate(b);
                profitList.add(billingReport1);
                otherData.add(billingReport);
            }
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

}
