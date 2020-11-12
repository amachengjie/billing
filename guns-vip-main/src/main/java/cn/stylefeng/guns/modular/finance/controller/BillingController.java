package cn.stylefeng.guns.modular.finance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @RequestMapping("/info")
    public Object info(){
        return "/demos/excel_import.html";
    }

}
