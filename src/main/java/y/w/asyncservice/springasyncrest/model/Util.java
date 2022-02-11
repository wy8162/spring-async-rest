package y.w.asyncservice.springasyncrest.model;

import org.apache.commons.math3.random.RandomDataGenerator;

public class Util {
    public static void delayRandomly()
    {
        try
        {
            //long time = new RandomDataGenerator().nextLong(500L, 5000L);
            Thread.sleep(200);
        }
        catch (InterruptedException e) {}
    }
}
