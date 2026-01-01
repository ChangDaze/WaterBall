package PatientDatabase;

import PrescriptionHandler.Prescription;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PatientCase {
    private long caseTime; //UNIX時間
    private List<Symptom> symptoms;
    private Prescription prescription;
}
