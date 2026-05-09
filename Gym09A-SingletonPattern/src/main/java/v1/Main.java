package v1;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        // Create a vector of size 1000 filled with 1.0
        List<BigDecimal> vector = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            vector.add(new BigDecimal("1.0"));
        }

        // Execute 100 iterations asynchronously with separate Models and Model instances
        List<CompletableFuture<List<BigDecimal>>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CompletableFuture<List<BigDecimal>> future = CompletableFuture.supplyAsync(() -> {
                // Create a new RealModels instance for each "user"
                Models models = new RealModels();
                // Create a new Model instance for each "user"
                Model model = models.createModel("Reflection.mat");
                return model.linearTransformation(vector);
            });
            futures.add(future);
        }

        // Wait for all futures to complete and print results
        for (int i = 0; i < futures.size(); i++) {
            List<BigDecimal> result = futures.get(i).get();
            System.out.println("Iteration " + (i + 1) + ": " + result);
        }
    }
}
