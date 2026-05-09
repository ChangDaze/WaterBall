package v1;

import java.math.BigDecimal;
import java.util.List;

public interface Model {
    List<BigDecimal> linearTransformation(List<BigDecimal> vector);
}
