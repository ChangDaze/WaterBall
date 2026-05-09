package v1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealModels implements Models {
    private static final Map<String, List<List<BigDecimal>>> _modelMap = new HashMap<>(); //static 確保共用

    public static List<List<BigDecimal>> getModelMatrix(String modelName){
        if (_modelMap.containsKey(modelName)){
            return _modelMap.get(modelName);
        } else {
            //lazy loading
            try {
                loadModel(modelName);
                return _modelMap.get(modelName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load model: " + modelName, e);
            }
        }
    }

    private static void loadModel(String modelName) throws IOException {
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

        _modelMap.put(modelName, matrix);
    }

    @Override
    public Model createModel(String modelName){
        return new RealModel(modelName);
    }
}
