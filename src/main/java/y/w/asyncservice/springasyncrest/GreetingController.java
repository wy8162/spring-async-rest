package y.w.asyncservice.springasyncrest;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import y.w.asyncservice.springasyncrest.model.Discount;
import y.w.asyncservice.springasyncrest.model.Quote;
import y.w.asyncservice.springasyncrest.model.Shop;

@RestController
public class GreetingController {
    @Autowired
    private ExecutorService executorService;

    private static final String template = "Hello, %s! Thread: %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public CompletableFuture<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        CompletableFuture<Greeting> result = CompletableFuture
            .supplyAsync(() -> createGreeting(name));

        return result;
    }

    @GetMapping("/async")
    public CompletableFuture<Greeting> asyncGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
       return getGreeting(name);
    }

    @GetMapping("/product")
    public CompletableFuture<List<String>> getProducts() {
        List<CompletableFuture<String>> priceFutures = shops
            .stream()
            //.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice("prod1"), executorService)) // Use our executor for much better performance
            .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice("prod1"))) // Use our ForkJoinPool common pool for much better performance
            .map(future -> future.thenApply(Quote::parse))
            .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote))))
            .collect(toList());

        return CompletableFuture.supplyAsync(() -> priceFutures.stream().map(CompletableFuture::join).collect(toList()));
    }

    @Async
    public CompletableFuture<Greeting> getGreeting(String name) {
        return CompletableFuture
            .supplyAsync(() -> createGreeting(name));
    }

    private Greeting createGreeting(String name) {
       return new Greeting(counter.incrementAndGet(), String.format(template, name, Thread.currentThread().getName()));
    }


    private static final List<Shop> shops = new ArrayList<>();

    static
    {
        shops.addAll(Arrays.asList(
            new Shop("Shop1"),
            new Shop("Shop2"),
            new Shop("Shop3"),
            new Shop("Shop4"),
            new Shop("Shop5"),
            new Shop("Shop6"),
            new Shop("Shop7"),
            new Shop("Shop8"),
            new Shop("Shop9"),
            new Shop("Shop10"),
            new Shop("Shop11"),
            new Shop("Shop12"),
            new Shop("Shop13")
        ));
    }

    private final ExecutorService getExecutor()
    {
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        return executor;
    }

}
