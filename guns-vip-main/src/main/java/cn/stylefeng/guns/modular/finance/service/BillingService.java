package cn.stylefeng.guns.modular.finance.service;

import cn.stylefeng.guns.modular.finance.entity.Billing;
import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BillingService {
    Page<Map<String, Object>> billingInfoList(String billingType, String startTime, String endTime);

    int saveBilling(Billing billing);

    List<BillingReport> getReportListBySix(Date newTime,Date oldTime);
}
