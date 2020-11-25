package cn.stylefeng.guns.modular.finance.service.impl;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import cn.stylefeng.guns.modular.finance.mapper.StatisticsMapper;
import cn.stylefeng.guns.modular.finance.service.StatisticsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;


    @Override
    public List<String> getReportDate(Date date, Long userId) {
        return statisticsMapper.getReportDate(date, userId);
    }

    @Override
    public List<BillingReport> getReportData(Date date, String type, Long userId) {
        return statisticsMapper.getReportData(date, type, userId);
    }

    @Override
    public List<Map<String, Object>> getPieMapData(Date date, Long userId) {
        return statisticsMapper.getPieMapData(date,userId);
    }
}
