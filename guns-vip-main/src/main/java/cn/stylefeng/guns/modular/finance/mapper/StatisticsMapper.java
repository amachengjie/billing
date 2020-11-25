package cn.stylefeng.guns.modular.finance.mapper;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticsMapper {

    List<String> getReportDate(@Param("date") Date date, @Param("userId") Long userId);

    List<BillingReport> getReportData(@Param("date") Date date, @Param("type") String type, @Param("userId") Long userId);

    List<Map<String, Object>> getPieMapData(@Param("date") Date date, @Param("userId") Long userId);
}
