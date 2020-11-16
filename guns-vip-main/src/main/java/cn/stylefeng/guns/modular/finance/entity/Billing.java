package cn.stylefeng.guns.modular.finance.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Billing {
    private Long id;
    private Long memberId;
    private String memberName;
    private String billingType;
    private String billingMemo;
    private BigDecimal billingPrice;
    private Date billingTime;
    private Date createDate;
}
