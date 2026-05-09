package v0;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Models models = new RealModels();
        Model model = models.createModel("example.mat");

        List<BigDecimal> vector = List.of(new BigDecimal("1"), new BigDecimal("2"));

        List<BigDecimal> result = model.linearTransformation(vector);
        System.out.println("Result of linear transformation: " + result);

    }
}
