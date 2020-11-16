package cn.stylefeng.guns.modular.finance.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillingReport {

    private BigDecimal billingAmount;
    private String date;
}
