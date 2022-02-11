package y.w.asyncservice.springasyncrest.model;

import static y.w.asyncservice.springasyncrest.model.Util.delayRandomly;

import java.math.BigDecimal;

public class Discount {
    public enum Code
    {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage)
        {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote)
    {
        return String.format("%s price is %.2f", quote.getStoreName(), Discount.apply(quote.getPrice(), quote.getDiscountCode()));
    }

    public static String applyDiscount(String price)
    {
        Quote quote = Quote.parse(price);
        return String.format("%s price is %.2f", quote.getStoreName(), Discount.apply(quote.getPrice(), quote.getDiscountCode()));
    }

    private static BigDecimal apply(BigDecimal price, Code code)
    {
        delayRandomly();

        BigDecimal discountedPrice = price.multiply(new BigDecimal((100.0-code.percentage)/100.0));
        //System.out.println(String.format("Price : %.2f Discount: %d, discounted price %.2f", price, code.percentage, discountedPrice));

        return discountedPrice;
    }

}
