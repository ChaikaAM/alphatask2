package com.alpha.task1.listener;

import com.alpha.task1.model.kafka.Payment;
import com.alpha.task1.service.AnaliticsStaticsStateHolder;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RawPaymentsKafkaListener {

    private final AnaliticsStaticsStateHolder analiticsStaticsStateHolder;

    @KafkaListener(topics = "RAW_PAYMENTS",
            topicPartitions = @TopicPartition(
                    topic = "RAW_PAYMENTS",
                    partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")))
    public void listenWithHeaders(@Payload Payment payment) {
        analiticsStaticsStateHolder.onMessage(payment);
    }
}
