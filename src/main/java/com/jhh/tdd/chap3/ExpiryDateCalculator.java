package com.jhh.tdd.chap3;

import jdk.vm.ci.meta.Local;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(PayData payData) {
        int addedMonths = payData.getPayAmount() >= 100_000 ?
                (payData.getPayAmount() / 100_000) * 12 + ((payData.getPayAmount() % 100_000) / 10_000) :
                payData.getPayAmount() / 10_000; // 코드 정리: 상수를 변수로
        if(payData.getFirstBillingDate() != null){
            return expiryDateUsingFirstBillingDate(payData, addedMonths);
        } else return payData.getBillingDate().plusMonths(addedMonths);
    }

    private LocalDate expiryDateUsingFirstBillingDate(PayData payData, int addedMonths){
        LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths); // 후보 만료일 구함
        final int dayOfFirstBilling = payData.getFirstBillingDate().getDayOfMonth();
        if(dayOfFirstBilling != candidateExp.getDayOfMonth()){ // 첫 납부일의 일자와 후보 만료일의 일자가 다르면
            //후보 만료일이 포함된 달의 마지막 날 < 첫 납부일의 일자
            final int dayLenOfCandiMon = YearMonth.from(candidateExp).lengthOfMonth();
            if(dayLenOfCandiMon <= payData.getFirstBillingDate().getDayOfMonth())
                return candidateExp.withDayOfMonth(dayLenOfCandiMon);
            return candidateExp.withDayOfMonth(dayOfFirstBilling);
        } else {
            return candidateExp;
        }
    }
}
