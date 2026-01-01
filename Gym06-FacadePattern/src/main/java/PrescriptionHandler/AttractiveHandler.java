package PrescriptionHandler;

import PatientDatabase.Patient;
import PatientDatabase.Symptom;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class AttractiveHandler extends PrescriptionHandler {

    @Override
    protected Boolean match(Patient patient, List<Symptom> symptoms){
        return patient.getAge() == 18 && symptoms.contains(Symptom.Sneeze);
    }

    @Override
    protected Prescription returnPrescription(){
        return Prescription.builder()
                .name("青春抑制劑")
                .potentialDisease("有人想你了 (專業學名：Attractive)")
                .medicines(List.of("假鬢角", "臭味"))
                .usage("把假鬢角黏在臉的兩側，讓自己異性緣差一點，自然就不會有人想妳了。")
                .build();
    }
}
