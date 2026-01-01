package PrescriptionHandler;

import PatientDatabase.Patient;
import PatientDatabase.Symptom;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Builder
public class SleepApneaSyndromeHandler extends PrescriptionHandler  {

    @Override
    protected Boolean match(Patient patient, List<Symptom> symptoms){
        BigDecimal height = patient.getHeight().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal bmi = patient.getWeight().divide(height.pow(2), 10, RoundingMode.HALF_UP);

        return bmi.compareTo(BigDecimal.valueOf(26)) > 0 && symptoms.contains(Symptom.Snore);
    }

    @Override
    protected Prescription returnPrescription(){
        return Prescription.builder()
                .name("打呼抑制劑")
                .potentialDisease("睡眠呼吸中止症（專業學名：SleepApneaSyndrome）")
                .medicines(List.of("一捲膠帶"))
                .usage("睡覺時，撕下兩塊膠帶，將兩塊膠帶交錯黏在關閉的嘴巴上，就不會打呼了。")
                .build();
    }
}
