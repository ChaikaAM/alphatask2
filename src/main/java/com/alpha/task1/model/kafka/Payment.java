package com.alpha.task1.model.kafka;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {

    private String ref;

    private Long categoryId;

    private String categtId;  //??? some messages in topic have this naming

    private String userId;

    private String recipientId;

    private String desc;

    private BigDecimal amount;

}
