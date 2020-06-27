package com.alpha.task1.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Data
@Builder
public class UserPaymentAnalytic {
    private Map<String, PaymentCategoryInfo> analyticInfo;
    private BigDecimal totalSum;
    private String userId;

    public static UserPaymentAnalytic empty(String userId) {
        return UserPaymentAnalytic.builder()
                .analyticInfo(Collections.emptyMap())
                .totalSum(BigDecimal.ZERO)
                .userId(userId)
                .build();
    }
}

