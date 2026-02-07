package ObserverPattern.v2;

public class Main {
    public static void main(String[] args){
        StudentDataFile studentDataFile = new StudentDataFile("ObserverPattern-student.data");
        StudentLanguageBarChart barChart = new StudentLanguageBarChart(studentDataFile);
        StudentJobTitlePieChart pieChart = new StudentJobTitlePieChart(studentDataFile);
        StudentDataFileBackup dataFileBackup = new StudentDataFileBackup(studentDataFile);
        studentDataFile.register(barChart);
        studentDataFile.register(pieChart);
        studentDataFile.register(dataFileBackup);
        studentDataFile.startMonitoring();
    }
}
