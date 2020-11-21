package cn.stylefeng.guns.modular.finance.mapper;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;

import java.util.Date;
import java.util.List;

public interface StatisticsMapper {

    List<String> getReportDate(Date date);

    List<BillingReport> getReportData(Date date, String type);
}
