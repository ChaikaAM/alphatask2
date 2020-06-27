package com.alpha.task1.service;

import com.alpha.task1.api.NotFoundException;
import com.alpha.task1.model.PaymentCategoryInfo;
import com.alpha.task1.model.UserPaymentAnalytic;
import com.alpha.task1.model.UserPaymentStats;
import com.alpha.task1.model.UserTemplate;
import com.alpha.task1.model.kafka.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnaliticsService {

    private final AnaliticsStaticsStateHolder analiticsStaticsStateHolder;

    public Collection<UserPaymentAnalytic> getAllAnalitics() {
        return analiticsStaticsStateHolder.getPayments().entrySet().stream()
                .map(it -> buildAnalitics(it.getKey(), it.getValue())).collect(Collectors.toList());

    }

    public UserPaymentAnalytic getAnalitics(String userId) {
        Collection<Payment> payments = analiticsStaticsStateHolder.getPayments().get(userId);
        if (CollectionUtils.isEmpty(payments)) {
            throw new NotFoundException();
        }
        return buildAnalitics(userId, payments);
    }

    public List<UserTemplate> getUserTemplates(String userId) {
        UserPaymentStats userPaymentStats = getCategoryStats(userId);
        Integer maxAmountCategoryId = userPaymentStats.getMaxAmountCategoryId();
        Integer oftenCategoryId = userPaymentStats.getOftenCategoryId();
        BigDecimal userAveragePayment = getUserAveragePayment(userId);
        return List.of(maxAmountCategoryId, oftenCategoryId).stream().map(it ->
                UserTemplate.builder()
                        .categoryId(it)
                        .amount(userAveragePayment)
                        .recipientId("ChaikaAM")
                        .build()).collect(Collectors.toList());
    }

    public UserPaymentStats getCategoryStats(String userId) {
        Collection<Payment> payments = analiticsStaticsStateHolder.getPayments().get(userId);
        if (CollectionUtils.isEmpty(payments)) {
            throw new NotFoundException();
        }
        Map<Long, List<Payment>> userPaymentsByCategoryId =
                payments.stream().collect(Collectors.groupingBy(Payment::getCategoryId));
        Map<Long, BigDecimal> totalPaymentsByCategory = userPaymentsByCategoryId.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                it -> it.getValue().stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));
        return UserPaymentStats.builder()
                .oftenCategoryId(userPaymentsByCategoryId.entrySet().stream().max(Comparator.comparingInt(one -> one.getValue().size())).map(Map.Entry::getKey).map(Long::intValue).orElse(null))
                .rareCategoryId(userPaymentsByCategoryId.entrySet().stream().min(Comparator.comparingInt(one -> one.getValue().size())).map(Map.Entry::getKey).map(Long::intValue).orElse(null))
                .maxAmountCategoryId(totalPaymentsByCategory.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).map(Long::intValue).orElse(null))
                .minAmountCategoryId(totalPaymentsByCategory.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).map(Long::intValue).orElse(null))
                .build();
    }

    private UserPaymentAnalytic buildAnalitics(String userId, Collection<Payment> userPayments) {
        Map<String, PaymentCategoryInfo> paymentInfosByCategory = userPayments.stream().collect(Collectors.groupingBy(Payment::getCategoryId))
                .entrySet().stream()
                .collect(Collectors.toMap(categoryPayments -> categoryPayments.getKey().toString(),
                        categoryPayments -> PaymentCategoryInfo.builder()
                                .max(categoryPayments.getValue().stream().map(Payment::getAmount).max(BigDecimal::compareTo).orElse(null))
                                .min(categoryPayments.getValue().stream().map(Payment::getAmount).min(BigDecimal::compareTo).orElse(null))
                                .sum(categoryPayments.getValue().stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                                .build()));
        return UserPaymentAnalytic.builder()
                .userId(userId)
                .totalSum(userPayments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                .analyticInfo(paymentInfosByCategory)
                .build();
    }

    private BigDecimal getUserAveragePayment(String userId) {
        List<BigDecimal> allUserPayments = analiticsStaticsStateHolder.getPayments().get(userId).stream().map(Payment::getAmount).collect(Collectors.toList());
        BigDecimal sum = allUserPayments.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(allUserPayments.size()), RoundingMode.DOWN);
    }
}
