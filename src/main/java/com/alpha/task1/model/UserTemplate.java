package com.alpha.task1.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserTemplate {
    private BigDecimal amount;
    private Integer categoryId;
    private String recipientId;
}

