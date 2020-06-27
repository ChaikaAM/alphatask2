package com.alpha.task1.service;

import com.alpha.task1.model.kafka.Payment;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Component
public class AnaliticsStaticsStateHolder {

    @Getter
    private Map<String, Collection<Payment>> payments = new ConcurrentHashMap<>();

    /**
     * Method called by {@link com.alpha.task1.listener.RawPaymentsKafkaListener}
     *
     * @param payment
     */
    public void onMessage(Payment payment) {
        payments.compute(payment.getUserId(), (userId, oldStatistics) -> {
            Collection<Payment> payments = ofNullable(oldStatistics).orElse(new ArrayList<>());
            payments.add(payment);
            return oldStatistics;
        });
    }
}
