package Prescriber;

import PatientDatabase.Symptom;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PrescriptionDemandTask {
    private final String id;
    private final List<Symptom> symptoms;
}
