package PatientDatabase;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class Patient {
    private String id;
    private String name;
    private Gender gender;
    private int age;
    private BigDecimal height;
    private BigDecimal weight;
    private List<PatientCase> patientCases;
}
