package org.mad.money.fp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

@Configuration
public class FpConfiguration {

    public Function<BigDecimal, BigDecimal> calculateFinalPriceForListingPrice(
            String discountRateString,
            String taxRateString,
            String listPriceString,
            BiFunction<BigDecimal, BigDecimal, Boolean> applyDiscountRuleFunction,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> applyDiscountFunction,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> applyTaxFunction,
            CalculateFinalPriceFunction calculateFinalPriceFunction
    ) {
        var discountRate = new BigDecimal(discountRateString);
        var taxRate = new BigDecimal(taxRateString);
        var listPrice = new BigDecimal(listPriceString);
        return generateCurriedCalculateFinalPrice(listPrice, discountRate, taxRate, applyDiscountRuleFunction, applyDiscountFunction, applyTaxFunction, calculateFinalPriceFunction);
    }

    private Function<BigDecimal, BigDecimal> generateCurriedCalculateFinalPrice(
            BigDecimal listPrice,
            BigDecimal discountRate,
            BigDecimal taxRate,
            BiFunction<BigDecimal, BigDecimal, Boolean> applyDiscountRule,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> applyDiscount,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> applyTax,
            CalculateFinalPriceFunction calculateFinalPriceFunction
    ) {
        var applyDiscountRuleForAmount = curry(applyDiscountRule).apply(listPrice);
        var applyDiscountForAmount = curry(applyDiscount).apply(discountRate);
        var applyTaxForAmount = curry(applyTax).apply(taxRate);
        var calculateFinalPriceForListingPrice = curry(calculateFinalPriceFunction)
                .apply(applyDiscountRuleForAmount)
                .apply(applyDiscountForAmount)
                .apply(applyTaxForAmount);
        return calculateFinalPriceForListingPrice;
    }

    private <T> Function<BigDecimal, Function<BigDecimal, T>>
    curry(BiFunction<BigDecimal, BigDecimal, T> function) {
        return t -> u -> function.apply(t, u);
    }

    private Function<Function<BigDecimal, Boolean>, Function<Function<BigDecimal, BigDecimal>, Function<Function<BigDecimal, BigDecimal>, Function<BigDecimal, BigDecimal>>>>
    curry(CalculateFinalPriceFunction function) {
        return applyDiscountRule -> applyDiscountForAmount -> applyTaxForAmount -> listingPrice ->
                function.apply(applyDiscountRule, applyDiscountForAmount, applyTaxForAmount, listingPrice);
    }
}
