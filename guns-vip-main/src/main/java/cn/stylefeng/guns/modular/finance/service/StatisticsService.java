package cn.stylefeng.guns.modular.finance.service;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;

import java.util.Date;
import java.util.List;

public interface StatisticsService {
    List<String> getReportDate(Date date);

    List<BillingReport> getReportData(Date date, String type);
}
