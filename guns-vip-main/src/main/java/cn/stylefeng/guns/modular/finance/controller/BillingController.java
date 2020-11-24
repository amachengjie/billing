package cn.stylefeng.guns.modular.finance.controller;

import cn.stylefeng.guns.base.auth.context.LoginContextHolder;
import cn.stylefeng.guns.base.auth.model.LoginUser;
import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.finance.entity.Billing;
import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import cn.stylefeng.guns.modular.finance.service.BillingService;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @RequestMapping("/info")
    public Object info() {
        return "/billing/billingInfo.html";
    }

    @RequestMapping("/info/saveBillingPage")
    public Object saveBilling() {
        return "/billing/billingInfoAdd.html";
    }

    @RequestMapping("/info/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String billingType
            , @RequestParam(required = false) String startTime
            , @RequestParam(required = false) String endTime) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        Page<Map<String, Object>> page = billingService.billingInfoList(billingType, startTime, endTime, user.getId());
        for (Map<String, Object> m : page.getRecords()) {
            if (ToolUtil.isNotEmpty(m.get("billing_type"))) {
                if ("repast".equals(m.get("billing_type"))) {
                    m.put("billing_type", "餐饮");
                }
                if ("traffic".equals(m.get("billing_type"))) {
                    m.put("billing_type", "交通");
                }
                if ("invest".equals(m.get("billing_type"))) {
                    m.put("billing_type", "投资");
                }
                if ("repayment".equals(m.get("billing_type"))) {
                    m.put("billing_type", "还款");
                }
                if ("online_shopping".equals(m.get("billing_type"))) {
                    m.put("billing_type", "网上购物");
                }
                if ("shopping".equals(m.get("billing_type"))) {
                    m.put("billing_type", "线下购物");
                }
                if ("income".equals(m.get("billing_type"))) {
                    m.put("billing_type", "收入");
                }
            }
        }
        return LayuiPageFactory.createPageInfo(page);
    }

    @RequestMapping("/info/saveBilling")
    @ResponseBody
    public Object saveBilling(Billing billing) {
        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();
        billing.setCreateDate(new Date());
        billing.setMemberId(user.getId());
        billing.setMemberName(user.getName());
        int count = billingService.saveBilling(billing);
        if (count > 0) {
            return ResponseData.success(0000, "保存账单成功", null);
        }
        return ResponseData.error("保存账单失败");
    }

    /**
     * 展示近六个月的花费
     *
     * @return
     */
    @RequestMapping("/info/getReportListBySix")
    @ResponseBody
    public Object getReportListBySix() {
        Map<String, Object> data = new HashMap<>();
        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -10);
        List<BillingReport> reports = billingService.getReportDate(new Date(), calendar.getTime(),user.getId());
        List<String> dateList = reports.stream().map(c -> c.getDate()).collect(Collectors.toList());

        List<BillingReport> repastList = billingService.getReportDataByType(new Date(), calendar.getTime(), "repast",user.getId());
        List<BillingReport> trafficList = billingService.getReportDataByType(new Date(), calendar.getTime(), "traffic",user.getId());
        List<BillingReport> investList = billingService.getReportDataByType(new Date(), calendar.getTime(), "invest",user.getId());
        List<BillingReport> repaymentList = billingService.getReportDataByType(new Date(), calendar.getTime(), "repayment",user.getId());
        List<BillingReport> online_shoppingList = billingService.getReportDataByType(new Date(), calendar.getTime(), "online_shopping",user.getId());
        List<BillingReport> shoppingList = billingService.getReportDataByType(new Date(), calendar.getTime(), "shopping",user.getId());

        List<String> reportDateList = repastList.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<String> trafficDateList = trafficList.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<String> investDateList = investList.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<String> repaymentDateList = repaymentList.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<String> online_shoppingDateList = online_shoppingList.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<String> shoppingDateList = shoppingList.stream().map(c -> c.getDate()).collect(Collectors.toList());

        //排序
        for(BillingReport b : reports){
            BillingReport billingReport =new BillingReport();
            billingReport.setBillingAmount(BigDecimal.ZERO);
            billingReport.setDate(b.getDate());
            billingReport.setTime(new Date());
            long count1 = reportDateList.stream().filter(c -> c.equals(b.getDate())).count();
            long count2 = trafficDateList.stream().filter(c -> c.equals(b.getDate())).count();
            long count3 = investDateList.stream().filter(c -> c.equals(b.getDate())).count();
            long count4 =  repaymentDateList.stream().filter(c -> c.equals(b.getDate())).count();
            long count5 = online_shoppingDateList.stream().filter(c -> c.equals(b.getDate())).count();
            long count6 =  shoppingDateList.stream().filter(c -> c.equals(b.getDate())).count();
            if (count1 <= 0) {
                repastList.add(billingReport);
            }
            if (count2 <= 0) {
                trafficList.add(billingReport);
            }
            if (count3 <= 0) {
                investList.add(billingReport);
            }
            if (count4 <= 0) {
                repaymentList.add(billingReport);
            }
            if (count5 <= 0) {
                online_shoppingList.add(billingReport);
            }
            if (count6 <= 0) {
                shoppingList.add(billingReport);
            }
        }

        List<BigDecimal> repastCollect = repastList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c->c.getBillingAmount()).collect(Collectors.toList());
        List<BigDecimal> trafficCollect = trafficList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c->c.getBillingAmount()).collect(Collectors.toList());
        List<BigDecimal> investCollect = investList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c->c.getBillingAmount()).collect(Collectors.toList());
        List<BigDecimal> repaymentCollect = repaymentList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c->c.getBillingAmount()).collect(Collectors.toList());
        List<BigDecimal> online_shoppingCollect = online_shoppingList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c->c.getBillingAmount()).collect(Collectors.toList());
        List<BigDecimal> shoppingCollect = shoppingList.stream().sorted(Comparator.comparing(BillingReport::getDate)).map(c->c.getBillingAmount()).collect(Collectors.toList());

        data.put("dateList", dateList);
        data.put("repastCollect", repastCollect);//餐饮
        data.put("trafficCollect", trafficCollect);//交通
        data.put("investCollect", investCollect);//投资
        data.put("repaymentCollect", repaymentCollect);//还款
        data.put("online_shoppingCollect", online_shoppingCollect);//网上购物
        data.put("shoppingCollect", shoppingCollect);//线下购物
        return ResponseData.success(0000, "", data);
    }
}
