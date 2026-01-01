package Prescriber;

import File.CsvFormat;
import File.JsonFormat;
import File.PlainTextFormat;
import PatientDatabase.*;
import PrescriptionHandler.*;
import lombok.Builder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Prescriber {
    private final PatientDatabase patientDatabase;
    private PrescriptionHandler prescriptionHandler;
    private final BlockingQueue<PrescriptionDemandTask> prescriptionDemandTask;
    private final List<PatientCase> patientCases;
    private final ExecutorService worker;
    private boolean running;

    public Prescriber(PatientDatabase patientDatabase)
    {
        this.patientDatabase = patientDatabase;
        prescriptionDemandTask = new LinkedBlockingQueue<>();
        patientCases = new ArrayList<>();
        worker = Executors.newSingleThreadExecutor();
        running = true;
        diagnose();
    }

    public void parsePrescriptions(String file){
        PlainTextFormat plainTextFormat = PlainTextFormat.builder().build();
        List<String> prescriptions = plainTextFormat.read(Path.of(file));

        // 先建立一個最頂層的 handler，這裡可以是一個「空 handler」或第一個 prescription
        if (prescriptions.isEmpty()) {
            prescriptionHandler = null;
            return;
        }

        // 用第一個 prescription 建立頭
        prescriptionHandler = ChoseHandler(prescriptions.getFirst());
        PrescriptionHandler currentHandler = prescriptionHandler;

        // 從第二個開始建立 chain
        for (int i = 1; i < prescriptions.size(); i++) {
            PrescriptionHandler nextHandler = ChoseHandler(prescriptions.get(i));
            currentHandler.setNext(nextHandler);
            currentHandler = nextHandler;
        }

        // 最後的 next 設為 null
        currentHandler.setNext(null);
    }

    private PrescriptionHandler ChoseHandler(String prescription){
        return switch (prescription) {
            case "COVID-19" -> COVID19Handler.builder().build();
            case "Attractive" -> AttractiveHandler.builder().build();
            case "SleepApneaSyndrome" -> SleepApneaSyndromeHandler.builder().build();
            default -> throw new RuntimeException("No handler rule matched.");
        };
    }

    public Boolean prescriptionDemand(String id, List<Symptom> symptoms){
        return prescriptionDemandTask.offer(PrescriptionDemandTask.builder().id(id).symptoms(symptoms).build());
    }

    public void diagnose(){
        worker.submit(() -> {
            while (running) {
                try {
                    PrescriptionDemandTask task = prescriptionDemandTask.take(); // 沒資料會阻塞
                    Patient patient = patientDatabase.getPatient(task.getId());
                    Prescription prescription = prescriptionHandler.diagnose(patient, task.getSymptoms());
                    PatientCase patientCase = PatientCase.builder()
                            .caseTime(Instant.now().getEpochSecond())
                            .symptoms(task.getSymptoms())
                            .prescription(prescription)
                            .build();
                    //印出診斷結果
                    System.out.println("[Diagnose] taskId=" + task.getId());
                    System.out.println("[Result] " + prescription.toString());
                    patientDatabase.insertCase(task.getId(), patientCase);//更新case
                    patientCases.add(patientCase);//export用
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void exportCases(String fileName, FileType fileType){
        switch (fileType){
            case FileType.Json:
                JsonFormat jsonFormat = JsonFormat.builder().build();
                jsonFormat.write(Paths.get(fileName), patientCases);
                break;
            case FileType.Csv:
                CsvFormat csvFormat = CsvFormat.builder().build();
                csvFormat.write(Paths.get(fileName), patientCases);
                break;
            default:
                throw new RuntimeException("No format rule matched.");
        }
    }
}
