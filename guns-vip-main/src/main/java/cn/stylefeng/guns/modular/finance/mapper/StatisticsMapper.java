package cn.stylefeng.guns.modular.finance.mapper;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatisticsMapper {

    List<String> getReportDate(@Param("date") Date date);

    List<BillingReport> getReportData(@Param("date") Date date, @Param("type") String type);
}
