package com.jhh.tdd.chap3;

import jdk.vm.ci.meta.Local;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
public class PayData {
    private LocalDate firstBillingDate;
    private LocalDate billingDate;
    private int payAmount;


    public PayData(LocalDate firstBillingDate, LocalDate billingDate, int payAmount) {
        this.firstBillingDate = firstBillingDate;
        this.billingDate = billingDate;
        this.payAmount = payAmount;
    }
}
