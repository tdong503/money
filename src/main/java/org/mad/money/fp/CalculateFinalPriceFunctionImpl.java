package org.mad.money.fp;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
public class CalculateFinalPriceFunctionImpl implements CalculateFinalPriceFunction {
    @Override
    public BigDecimal apply(
            Function<BigDecimal, Boolean> applyDiscountRule,
            Function<BigDecimal, BigDecimal> applyDiscount,
            Function<BigDecimal, BigDecimal> applyTax,
            BigDecimal listingPrice) {
        return when(applyDiscountRule).apply(applyDiscount).andThen(applyTax).apply(listingPrice);
    }

    private Function<Function<BigDecimal, BigDecimal>, Function<BigDecimal, BigDecimal>> when(Function<BigDecimal, Boolean> applyDiscountRule) {
        return applyDiscount -> listingPrice -> {
            if(applyDiscountRule.apply(listingPrice))
                return applyDiscount.apply(listingPrice);
            else
                return listingPrice;
        };
    }
}
