package PrescriptionHandler;

import PatientDatabase.Patient;
import PatientDatabase.Symptom;
import lombok.Builder;

import java.util.List;

@Builder
public class COVID19Handler extends PrescriptionHandler {

    @Override
    protected Boolean match(Patient patient, List<Symptom> symptoms){
        return symptoms.contains(Symptom.Headache) && symptoms.contains(Symptom.Cough);
    }

    @Override
    protected Prescription returnPrescription(){
        return Prescription.builder()
                .name("清冠一號")
                .potentialDisease("新冠肺炎（專業學名：COVID-19）")
                .medicines(List.of("清冠一號"))
                .usage("將相關藥材裝入茶包裡，使用500 mL 溫、熱水沖泡悶煮1~3 分鐘後即可飲用。")
                .build();
    }
}
