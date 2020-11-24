package cn.stylefeng.guns.modular.finance.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.finance.entity.Billing;
import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import cn.stylefeng.guns.modular.finance.mapper.BillingMapper;
import cn.stylefeng.guns.modular.finance.service.BillingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BillingServiceImpl implements BillingService {

    @Resource
    private BillingMapper billingMapper;

    @Override
    public Page<Map<String, Object>> billingInfoList(String billingType, String startTime, String endTime, Long userId) {
        Page page = LayuiPageFactory.defaultPage();
        return billingMapper.list(page, billingType, startTime, endTime, userId);
    }

    @Override
    public int saveBilling(Billing billing) {
        return billingMapper.saveBilling(billing);
    }

    @Override
    public List<BillingReport> getReportDate(Date newTime, Date oldTime, Long userId) {
        return billingMapper.getReportDate(newTime, oldTime, userId);
    }

    @Override
    public List<BillingReport> getReportDataByType(Date date, Date time, String type, Long userId) {
        return billingMapper.getReportDataByType(date, time, type, userId);
    }
}
