package ObserverPattern.v2;

import ObserverPattern.common.ReadStudents;
import ObserverPattern.common.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ObserverPattern.common.Utils.delay;

public class StudentDataFile {
    private boolean monitoring;
    private final String studentDataFileName;
    private Set<Student> students = new HashSet<>();
    private final List<StudentDataObserver> observers = new ArrayList<>();

    public StudentDataFile(String studentDataFileName){
        this.studentDataFileName = studentDataFileName;
    }

    public void startMonitoring(){
        monitoring = true;
        new Thread(this::monitoring).start();
    }

    public void register(StudentDataObserver observer) {
        this.observers.add(observer);
    }

    public void unregister(StudentDataObserver observer) {
        this.observers.remove(observer);
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
            notifyObservers();
        }
    }

    private void notifyObservers() throws IOException {
        for (StudentDataObserver observer : observers) {
            observer.update();
        }
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void stopMonitoring(){monitoring = false;}
}
