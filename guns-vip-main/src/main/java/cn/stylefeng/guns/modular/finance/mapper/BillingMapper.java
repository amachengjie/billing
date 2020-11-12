package cn.stylefeng.guns.modular.finance.mapper;

import cn.stylefeng.guns.modular.finance.entity.Billing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

public interface BillingMapper extends BaseMapper<Billing> {
    Page<Map<String, Object>> list(Page page);
}
