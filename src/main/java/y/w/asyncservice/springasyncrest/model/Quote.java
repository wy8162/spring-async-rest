package y.w.asyncservice.springasyncrest.model;

import java.math.BigDecimal;

public class Quote {
    private final String storeName;
    private final String productName;
    private final BigDecimal price;
    private final Discount.Code discountCode;

    public Quote(String storeName, String productName, BigDecimal price, Discount.Code discountCode)
    {
        this.storeName = storeName;
        this.productName = productName;
        this.price = price;
        this.discountCode = discountCode;
    }

    public static Quote parse(String s)
    {
        String[] split = s.split(":");
        String shopName = split[0];
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(split[1]));
        Discount.Code discountCode = Discount.Code.valueOf(split[2]);
        String productName = split[3];
        return new Quote(shopName, productName, price, discountCode);
    }

    public String getStoreName()
    {
        return storeName;
    }

    public String getProductName()
    {
        return productName;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public Discount.Code getDiscountCode()
    {
        return discountCode;
    }
}
