package cn.stylefeng.guns.modular.finance.service;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    List<String> getReportDate(Date date, Long userId);

    List<BillingReport> getReportData(Date date, String type, Long userId);

    List<Map<String, Object>> getPieMapData(Date date, Long userId);
}
