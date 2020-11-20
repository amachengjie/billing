package cn.stylefeng.guns.modular.finance.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillingReport {

    private BigDecimal billingAmount;
    private String date;
    private Date time;
}
