package com.alpha.task1.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentCategoryInfo {
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal sum;
}

