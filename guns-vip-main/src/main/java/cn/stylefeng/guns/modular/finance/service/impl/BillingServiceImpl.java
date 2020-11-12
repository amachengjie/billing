package cn.stylefeng.guns.modular.finance.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.finance.mapper.BillingMapper;
import cn.stylefeng.guns.modular.finance.service.BillingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class BillingServiceImpl implements BillingService {

    @Resource
    private BillingMapper billingMapper;

    @Override
    public Page<Map<String, Object>> billingInfoList() {
        Page page = LayuiPageFactory.defaultPage();
        return billingMapper.list(page);
    }
}
