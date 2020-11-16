package cn.stylefeng.guns.modular.finance.mapper;

import cn.stylefeng.guns.modular.finance.entity.Billing;
import cn.stylefeng.guns.modular.finance.entity.BillingReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BillingMapper extends BaseMapper<Billing> {
    Page<Map<String, Object>> list(Page page,String billingType, String startTime, String endTime);

    int saveBilling(Billing billing);

    List<BillingReport> getReportListBySix(@Param("newTime") Date newTime,@Param("oldTime")Date oldTime);
}
