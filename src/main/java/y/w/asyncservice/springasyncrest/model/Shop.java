package y.w.asyncservice.springasyncrest.model;

import static y.w.asyncservice.springasyncrest.model.Util.delayRandomly;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import org.apache.commons.math3.random.RandomDataGenerator;
import y.w.asyncservice.springasyncrest.model.Discount.Code;

public class Shop {
    Logger log = Logger.getLogger(Shop.class.getName());

    private String storeName;

    private Map<String, Product> products = new HashMap<>();

    public Shop(String storeName)
    {
        List<Code> discounts = Arrays.asList(
            Discount.Code.NONE,
            Discount.Code.SILVER,
            Discount.Code.GOLD,
            Discount.Code.PLATINUM,
            Discount.Code.DIAMOND);

        Collections.shuffle(discounts);

        this.storeName = storeName;
        for (int i = 1; i<5; i++)
        {
            String name = "prod" + i;

            Product p = new Product(
                name,
                discounts.get(new RandomDataGenerator().nextInt(0, discounts.size() - 1)),
                new BigDecimal(new RandomDataGenerator().nextLong(1L, 100L)));
            products.put(name, p);
        }
    }

    public String getStoreName()
    {
        return storeName;
    }

    /**
     * Blocking, old fashioned, method call.
     *
     * @param productName
     * @return "storeName:price:discountCode"
     */
    public String getPrice(String productName)
    {
        log.info(storeName + "----> Inside future task...begin, to wait for random time: " + Thread.currentThread().getName());

        Product prod = products.get(productName);

        delayRandomly();
        log.info(storeName + "----> Inside future task...finished");

        // storeName:price:discountCode:productName
        return String.format("%s:%.2f:%s:%s", storeName, prod.price, prod.discountCode, prod.productName);
    }

    /* Different ways to make getPrice async. */
    /**
     * Make the getPrice asynchronous.
     *
     * @param productName
     * @return
     */
    public Future<String> getPriceAsync(String productName)
    {
        CompletableFuture<String> futurePrice = new CompletableFuture<>();

        new Thread( () -> {
            String price = getPrice(productName);
            futurePrice.complete(price);
        }).start();

        return futurePrice;
    }

    /**
     * Using the handy factory methods provided by CompletableFuture instead. Much simpler.
     *
     * The supplyAsync method accepts a Supplier as argument and returns a Completable-Future that
     * will be asynchronously completed with the value obtained by invoking that Supplier. This
     * Supplier will be run by one of the Executors in the ForkJoinPool, but you can specify a different
     * Executor by passing it as a second argument to the overloaded version of this method
     *
     * @param productName
     * @return
     */
    public Future<String> getPriceAsyncCompletableFuture(String productName)
    {
        return CompletableFuture.supplyAsync(() -> getPrice(productName));
    }
}
