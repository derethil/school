import java.util.concurrent.CountDownLatch;

public class CalculateDigits implements Runnable {
    private TaskQueue queue;
    private ResultTable results;
    private CountDownLatch latch;

    public CalculateDigits(TaskQueue queue, ResultTable results, CountDownLatch latch) {
        this.queue = queue;
        this.results = results;
        this.latch = latch;
    }

    @Override
    public void run() {
        Bpp bpp = new Bpp();

        while (queue.size() > 0) {
            int digitToCalculate = queue.pop();
            int result = bpp.getDecimal(digitToCalculate);
            results.put(digitToCalculate, result);

            if (results.size() % 10 == 0) System.out.print(".");
        }

        latch.countDown();
    }
}
