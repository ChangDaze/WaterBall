package v0;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RealModels implements Models {
    @Override
    public Model createModel(String modelName) throws IOException {
        List<List<BigDecimal>> matrix = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(modelName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                List<BigDecimal> row = new ArrayList<>();
                String[] values = line.split("[,\\s]+");

                for (String value : values) {
                    row.add(new BigDecimal(value));
                }

                matrix.add(row);
            }
        }

        return new RealModel(matrix);
    }
}
