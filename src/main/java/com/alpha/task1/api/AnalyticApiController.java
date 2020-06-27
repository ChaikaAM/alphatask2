package com.alpha.task1.api;

import com.alpha.task1.model.UserPaymentAnalytic;
import com.alpha.task1.model.UserPaymentStats;
import com.alpha.task1.model.UserTemplate;
import com.alpha.task1.service.AnaliticsService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-06-27T09:13:50.157Z")

@RestController
@RequestMapping("/analytic")
@AllArgsConstructor
public class AnalyticApiController {

    private final AnaliticsService analiticsService;

    @GetMapping
    public ResponseEntity<Collection<UserPaymentAnalytic>> getAllAnalyticUsingGET() {
        return ResponseEntity.ok(analiticsService.getAllAnalitics());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPaymentAnalytic> getUserAnalyticUsingGET(@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId) {
        return ResponseEntity.ok(analiticsService.getAnalitics(userId));
    }

    @GetMapping("/{userId}/stat")
    public ResponseEntity<UserPaymentStats> getPaymentStatsByUserUsingGET(@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId) {
        return ResponseEntity.ok(analiticsService.getCategoryStats(userId));
    }


    @GetMapping("/{userId}/templates")
    public ResponseEntity<List<UserTemplate>> getUserTemplatesUsingGET(@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId) {
        return ResponseEntity.ok(analiticsService.getUserTemplates(userId));
    }

}
