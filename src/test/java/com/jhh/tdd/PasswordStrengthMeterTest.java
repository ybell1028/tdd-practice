package com.jhh.tdd;

import com.jhh.tdd.chap2.PasswordStrength;
import com.jhh.tdd.chap2.PasswordStrengthMeter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class PasswordStrengthMeterTest {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expStr){
        PasswordStrength result = meter.meter(password);
        assertThat(result).isEqualTo(expStr);
    }

    @Test
    void meetsAllCriteria_Then_Strong(){
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal(){
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abcAdd", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOtherCriteria_except_for_number_Then_Normal(){
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    @Test
    void nullInput_Then_Invalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    void emptyInput_Then_Invalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    void meetsOtherCriteria_except_for_Uppercase_Then_Normal() {
        assertStrength("ab12!@df", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOnlyLengthCriteria_Then_Weak() {
        assertStrength("abcdefghi", PasswordStrength.WEAK);
    }
}
