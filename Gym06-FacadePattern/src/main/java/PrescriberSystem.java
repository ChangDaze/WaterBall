import PatientDatabase.*;
import Prescriber.*;
import lombok.*;

import java.util.List;

@Getter
public class PrescriberSystem {
    private final PatientDatabase patientDatabase;
    private final  Prescriber prescriber;

    public PrescriberSystem()
    {
        patientDatabase = PatientDatabase.builder().build();
        prescriber = new Prescriber(patientDatabase);
    }

    public void setSystem(String patientJson, String prescriptionFile){
        patientDatabase.parsePatients(patientJson);
        prescriber.parsePrescriptions(prescriptionFile);
    }

    public Boolean prescriptionDemand(String id, List<Symptom> symptoms){
        return prescriber.prescriptionDemand(id, symptoms);
    }

    public void exportCases(String fileName, FileType fileType){
        prescriber.exportCases(fileName, fileType);
    }
}
