package ObserverPattern.v1;

public class Main {
    public static void main(String[] args){
        StudentDataFile studentDataFile = new StudentDataFile("ObserverPattern-student.data");
        studentDataFile.startMonitoring();
    }
}
