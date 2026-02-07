package ObserverPattern.v1;

import ObserverPattern.common.ReadStudents;
import ObserverPattern.common.Student;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ObserverPattern.common.Utils.delay;

public class StudentDataFile {
    private boolean monitoring;
    private final String studentDataFileName;
    private Set<Student> students = new HashSet<>();
    private final StudentJobTitlePieChart jobTitlePieChart = new StudentJobTitlePieChart(this);
    private final StudentLanguageBarChart languageBarChart = new StudentLanguageBarChart(this);
    private final StudentDataFileBackup dataFileBackup = new StudentDataFileBackup(this);

    public StudentDataFile(String studentDataFileName){
        this.studentDataFileName = studentDataFileName;
    }

    public void startMonitoring(){
        monitoring = true;
        new Thread(this::monitoring).start();
    }

    private void monitoring() {
        while (monitoring) {
            delay(1000);
            try {
                watchStudentData();
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }

    private void watchStudentData() throws IOException {
        Set<Student> newStudents = new HashSet<>(ReadStudents.fromFile(studentDataFileName));
        if(!this.students.equals(newStudents)) {
            this.students = newStudents;
            notifyCharts();
        }
    }

    private void notifyCharts() throws IOException {
        languageBarChart.renderBarChart();
        jobTitlePieChart.renderPieChart();
        dataFileBackup.backup();
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void stopMonitoring(){monitoring = false;}
}
