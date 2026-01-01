package PatientDatabase;

import File.JsonFormat;
import lombok.Builder;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class PatientDatabase {
    private Map<String, Patient> map;

    public void parsePatients(String file){
        JsonFormat jsonFormat =  JsonFormat.builder().build();
        List<Patient> data = jsonFormat.<Patient>read(Path.of(file), Patient.class);
        this.map = data.stream().collect(Collectors.toMap(Patient::getId, patient -> patient));
    }

    public Patient getPatient(String id){
        return map.get(id);
    }

    public void insertCase(String id, PatientCase patientCase){
        Patient patient = map.get(id);
        patient.getPatientCases().add(patientCase);
    }
}
