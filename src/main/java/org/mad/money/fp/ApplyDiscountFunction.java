package org.mad.money.fp;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.BiFunction;

@Component
public class ApplyDiscountFunction implements BiFunction<BigDecimal, BigDecimal, BigDecimal> {
    @Override
    public BigDecimal apply(BigDecimal rate, BigDecimal amount) {
        //if(amount.compareTo(BigDecimal.valueOf(100)) != 1) {
        //    System.out.println("discount only available over 100");
        //    return amount;
        //}
        
        var discount = amount.multiply(rate);
        return amount.add(discount.negate());
    }
}
