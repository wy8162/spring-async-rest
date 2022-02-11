package y.w.asyncservice.springasyncrest.model;

import java.math.BigDecimal;

public class Product {
    public String productName;
    public Discount.Code discountCode;
    public BigDecimal price;

    public Product(String product, Discount.Code discountCode, BigDecimal price)
    {
        this.productName = product;
        this.discountCode = discountCode;
        this.price = price;
    }

    @Override
    public String toString()
    {
        return String.format("%.2f:%s:%s", price, discountCode, productName);
    }
}
