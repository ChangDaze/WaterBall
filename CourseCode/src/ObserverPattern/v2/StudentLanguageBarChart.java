package ObserverPattern.v2;

import ObserverPattern.common.BarChart;
import ObserverPattern.common.Student;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static ObserverPattern.common.Utils.count;
import static ObserverPattern.common.Utils.selectDistinct;

public class StudentLanguageBarChart implements StudentDataObserver{
    private final StudentDataFile dataFile;

    public StudentLanguageBarChart(StudentDataFile dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public void update() throws IOException {
        BarChart barChart = new BarChart();
        Collection<Student> students = dataFile.getStudents();
        List<String> x = selectDistinct(students, Student::getLanguage);
        List<Integer> y = count(x, students, Student::getLanguage);
        barChart.export("ObserverPattern-students.bar.png", x, y);
    }
}
