package PrescriptionHandler;

import PatientDatabase.Patient;
import PatientDatabase.Symptom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
public abstract class PrescriptionHandler {
    private PrescriptionHandler next;

    public Prescription diagnose(Patient patient, List<Symptom> symptoms) {
        if(match(patient, symptoms)){
            return returnPrescription();
        } else if (next != null) {
            return next.diagnose(patient, symptoms);
        } else {
            System.out.println("No handler rule matched.");
            throw new RuntimeException("No handler rule matched.");
        }
    }

    protected abstract Boolean match(Patient patient, List<Symptom> symptoms);

    protected abstract Prescription returnPrescription();
}
