package com.alpha.task1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPaymentStats {
    private Integer maxAmountCategoryId;
    private Integer minAmountCategoryId;
    private Integer oftenCategoryId;
    private Integer rareCategoryId;
}

