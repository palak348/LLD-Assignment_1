import com.ratelimiter.client.ExternalServiceCaller;
import com.ratelimiter.factory.RateLimiterFactory;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String customerId = "User-A";

        System.out.println("=========================================");
        System.out.println("  TESTING FIXED WINDOW COUNTER");
        System.out.println("=========================================");

        // Rule: Max 3 requests per 1000ms (1 second)
        ExternalServiceCaller fixedLimiterService = new ExternalServiceCaller(
                RateLimiterFactory.Algorithm.FIXED_WINDOW, 3, 1000);

        for (int i = 1; i <= 5; i++) {
            System.out.print("Request " + i + ": ");
            fixedLimiterService.processClientRequest(customerId, true);
            Thread.sleep(100);
        }

        System.out.println("\n[Waiting 1 second for the window to reset...]\n");
        Thread.sleep(1000);

        System.out.print("Request 6 (After Reset): ");
        fixedLimiterService.processClientRequest(customerId, true);

        System.out.println("\n=========================================");
        System.out.println("  TESTING SLIDING WINDOW COUNTER");
        System.out.println("=========================================");

        // Rule: Max 3 requests per 1000ms (1 second)
        ExternalServiceCaller slidingLimiterService = new ExternalServiceCaller(
                RateLimiterFactory.Algorithm.SLIDING_WINDOW_COUNTER, 3, 1000);

        for (int i = 1; i <= 5; i++) {
            System.out.print("Request " + i + ": ");
            slidingLimiterService.processClientRequest(customerId, true);
            Thread.sleep(100);
        }
    }
}