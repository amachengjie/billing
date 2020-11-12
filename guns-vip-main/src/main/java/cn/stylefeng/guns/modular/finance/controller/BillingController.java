package cn.stylefeng.guns.modular.finance.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.finance.service.BillingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @RequestMapping("/info")
    public Object info(){
        return "/billing/billingInfo.html";
    }


    @RequestMapping("/info/list")
    @ResponseBody
    public Object list(){
       Page<Map<String,Object>> page = billingService.billingInfoList();
        return LayuiPageFactory.createPageInfo(page);
    }


}
