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
        Page<Map<String, Object>> page = billingService.billingInfoList(billingType, startTime, endTime);
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
                if ("online_shopping".equals(m.get("billing_type"))) {
                    m.put("billing_type", "网上购物");
                }
                if ("shopping".equals(m.get("billing_type"))) {
                    m.put("billing_type", "线下购物");
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        List<BillingReport> reports = billingService.getReportListBySix(new Date(),calendar.getTime());
        List<String> dateList = reports.stream().map(c -> c.getDate()).collect(Collectors.toList());
        List<BigDecimal> billingAmountList = reports.stream().map(c -> c.getBillingAmount()).collect(Collectors.toList());
        data.put("dateList", dateList);
        data.put("billingAmountList", billingAmountList);
        return ResponseData.success(0000, "", data);
    }
}
