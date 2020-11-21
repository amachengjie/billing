package cn.stylefeng.guns.modular.finance.service.impl;

import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import cn.stylefeng.guns.modular.finance.mapper.StatisticsMapper;
import cn.stylefeng.guns.modular.finance.service.StatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;


    @Override
    public List<String> getReportDate(Date date) {
        return statisticsMapper.getReportDate(date);
    }

    @Override
    public List<BillingReport> getReportData(Date date, String type) {
        return statisticsMapper.getReportData(date,type);
    }
}
