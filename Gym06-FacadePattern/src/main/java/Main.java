import PatientDatabase.Symptom;
import Prescriber.FileType;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PrescriberSystem prescriberSystem = new PrescriberSystem();
        prescriberSystem.setSystem(
                "C:\\Repo\\Java\\WaterBall\\Gym06-FacadePattern\\src\\test\\resources\\Patients",
                "C:\\Repo\\Java\\WaterBall\\Gym06-FacadePattern\\src\\test\\resources\\Prescriptions"
        );
        prescriberSystem.prescriptionDemand("P001", List.of(Symptom.Headache, Symptom.Cough));
        prescriberSystem.prescriptionDemand("P002", List.of(Symptom.Sneeze));
        prescriberSystem.prescriptionDemand("P003", List.of(Symptom.Snore));

        Thread.sleep(15000);

        prescriberSystem.exportCases("C:\\Repo\\Java\\WaterBall\\Gym06-FacadePattern\\src\\test\\resources\\cases.json", FileType.Json);
        prescriberSystem.exportCases("C:\\Repo\\Java\\WaterBall\\Gym06-FacadePattern\\src\\test\\resources\\cases.csv", FileType.Csv);
        System.out.println("finish");
    }
}
