package org.mad.money.oo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountImpl implements Discount {
    private final BigDecimal rate;

    public DiscountImpl(@Value("${money.discount.rate}") String rateString) {
        this.rate = new BigDecimal(rateString);
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.valueOf(100)) == 1) {
            var discount = amount.multiply(rate);
            return amount.add(discount.negate());
        }
        else
        {
            System.out.println("discount only available over 100");
            return amount;
        }
    }
}
