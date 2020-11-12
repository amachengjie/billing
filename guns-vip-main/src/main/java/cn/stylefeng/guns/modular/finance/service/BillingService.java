package cn.stylefeng.guns.modular.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

public interface BillingService {
    Page<Map<String, Object>> billingInfoList();
}
