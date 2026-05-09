package v0;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RealModel implements Model {
    private final List<List<BigDecimal>> matrix;

    public RealModel(List<List<BigDecimal>> matrix) {
        this.matrix = matrix;
    }

    @Override
    public List<BigDecimal> linearTransformation(List<BigDecimal> vector) {
        List<BigDecimal> result = new ArrayList<>();

        int numCols = matrix.getFirst().size();

        //「左列乘右行，對應相加」
        // 只有當 左矩陣的行數（寬度） 等於 右矩陣的列數（高度） 時，矩陣乘法才有定義。
        for (int col = 0; col < numCols; col++) {
            //Loop matrix's columns
            BigDecimal sum = BigDecimal.ZERO;

            // row = matrix's row index, vector's column index
            // col = matrix's column index
            // vector[row] = sum of (vector[row] * matrix[row][col])
            for (int row = 0; row < matrix.size(); row++) {
                //Loop matrix's row to get matrix[row][col]
                sum = sum.add(vector.get(row).multiply(matrix.get(row).get(col)));
            }

            result.add(sum);
        }

        return result;
    }
}
