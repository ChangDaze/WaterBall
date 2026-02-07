package ObserverPattern.v2;

import ObserverPattern.common.PieChart;
import ObserverPattern.common.Student;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static ObserverPattern.common.Utils.count;
import static ObserverPattern.common.Utils.selectDistinct;

public class StudentJobTitlePieChart implements StudentDataObserver{
    private final StudentDataFile dataFile;

    public StudentJobTitlePieChart(StudentDataFile dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public void update() throws IOException {
        Collection<Student> students = dataFile.getStudents();
        List<String> series = selectDistinct(students, Student::getJobTitle);
        List<Integer> numbers = count(series, students, Student::getJobTitle);
        PieChart pieChart = new PieChart();
        pieChart.export("ObserverPattern-students.pie.png", series, numbers);
    }
}
