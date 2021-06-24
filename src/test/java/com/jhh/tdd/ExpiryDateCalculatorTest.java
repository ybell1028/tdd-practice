package com.jhh.tdd;

import com.jhh.tdd.chap3.ExpiryDateCalculator;
import com.jhh.tdd.chap3.PayData;
import jdk.vm.ci.meta.Local;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨(){
        /*//쉬운 것부터 테스트
        LocalDate billingDate = LocalDate.of(2019, 3, 1);
        int payAmount = 10_000;

        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate expiryDate = cal.calculateExpiryDate(billingDate, payAmount);

        assertThat(expiryDate).isEqualTo(LocalDate.of(2019, 4, 1));

        //예를 추가하면서 구현을 일반화
        LocalDate billingDate2 = LocalDate.of(2019, 5, 5);
        int payAmount2 = 10_000;

        ExpiryDateCalculator cal2 = new ExpiryDateCalculator();
        LocalDate expiryDate2 = cal2.calculateExpiryDate(billingDate2, payAmount2);

        assertThat(expiryDate2).isEqualTo(LocalDate.of(2019, 6, 5));*/
        
        //코드 정리 : 중복 제거
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 3, 1))
                .payAmount(10_000)
                .build(), LocalDate.of(2019, 4, 1));
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 5, 5))
                .payAmount(10_000)
                .build()
                , LocalDate.of(2019, 6, 5));
    }

    @Test
        //예외 상황 처리 - 단순히 한달 추가로 끝나지 않는 상황
    void 납부일과_한달_뒤_일자가_같지_않음(){
        //LocalDate#plubMonths() 메서드가 알아서 한달 추가 처리를 해준 것이다.
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 1, 31))
                .payAmount(10_000)
                .build(), LocalDate.of(2019, 2, 28));
    }

    @Test
        //다음 테스트 선택 : 다시 예외 상황
    //1만원을 납부할 때 한달 뒤가 만료일이 되는 테스트를 진행했으니 그 다음으로 쉽거나 예외 적인 것을 선택
    //2만원을 지불하면 만료일이 두 달 뒤가 되고, 3만원을 지불하면 만료일이 세 달 뒤가 된다.
    void 첫_납부일과_만료일_일자가_다를때_만원_납부(){
        //이전 테스트가 1개월 요금 지불을 기준으로 하므로 1개월 요금 지불에 대한 예외 상황을 마무리하고 2개월 이상 요금 지불을 테스트 하는 것이 좋을 것 같다.
        //예외 상황을 테스트 하려면 첫  납부일이 필요하다. 앞서 작성한 테스트는 납부일과 납부액만 사용했기 때문에
        //기존 코드에 첫 납부일을 추가애햐한다.

        //메서드의 파라미터 개수가 세 개 이상이면 객체로 바꿔 한 개로 줄이는 것을 고려해야한다.3

        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 1, 31))
                .billingDate(LocalDate.of(2019, 2, 28))
                .payAmount(10_000)
                .build();
        // 첫 납부일이 2019.01.30이고 만료되는 2019.02.28에 1만원을 더 납부하면 다음 만료일은 2019.03.30이다.
        assertExpiryDate(payData, LocalDate.of(2019, 3, 31));

        PayData payData2 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 1, 30))
                .billingDate(LocalDate.of(2019, 2, 28))
                .payAmount(10_000)
                .build();
        assertExpiryDate(payData2, LocalDate.of(2019, 3, 30));

        PayData payData3 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 5, 31))
                .billingDate(LocalDate.of(2019, 6, 30))
                .payAmount(10_000)
                .build();
        assertExpiryDate(payData3, LocalDate.of(2019, 7, 31));
    }

    //다음 테스트 선택 쉬운 테스트
    //2만 원을 지불하면 만료일이 두달 뒤가 된다.
    //3만 원을 지불하면 만료일이 석달 뒤가 된다.
    @Test
    void 이만원_이상_납부하면_비례해서_만료일_계산(){
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 3, 1))
                .payAmount(20_000)
                .build(), LocalDate.of(2019, 5, 1));

        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 3, 1))
                .payAmount(20_000)
                .build(), LocalDate.of(2019, 5, 1));
    }

    //예외 상황 테스트 추가
    //첫 납부일과 납부일의 일자가 다를 때 2만 원 이상 납부한 경우

    @Test
    void 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부(){
        //그대로 진행하면 Invalid date 'APRIL 31' Exception이 나는데 이는 4월에 31일이 없기 때문
        assertExpiryDate(PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 1, 31))
                .billingDate(LocalDate.of(2019, 2, 28))
                .payAmount(20_000)
                .build(), LocalDate.of(2019, 4, 30));

        assertExpiryDate(PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 1, 31))
                .billingDate(LocalDate.of(2019, 2, 28))
                .payAmount(40_000)
                .build(), LocalDate.of(2019, 6, 30));

        assertExpiryDate(PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 3, 31))
                .billingDate(LocalDate.of(2019, 4, 30))
                .payAmount(30_000)
                .build(), LocalDate.of(2019, 7, 31)); //? 책엔 31일인데
    }

    //10개월 요금을 납부하면 1년 제공
    @Test
    void 십만원을_납부하면_1년_제공(){
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 1, 28))
                .payAmount(100_000)
                .build(), LocalDate.of(2020, 1, 28));
    }

    //다음으로 생각할 수 있는 테스트는 2020년 2월 29일과 같은 윤달 마지막 날에 10만원을 납부하는 상황
    //13만원을 납부하는 경우 1년 3개월 뒤가 만료일이 되어야 한다.

    //10개월 요금을 납부하면 1년 제공
    @Test
    void 십삼만원을_납부하면_1년3개월_제공(){
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019, 1, 28))
                .payAmount(130_000)
                .build(), LocalDate.of(2020, 4, 28));
    }

    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate){
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);
        assertThat(realExpiryDate).isEqualTo(expectedExpiryDate);
    }
}
