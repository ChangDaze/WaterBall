package TemplateMethodPattern.v2;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.GroupingStrategy;
import TemplateMethodPattern.common.ReadStudents;
import TemplateMethodPattern.common.Student;

import java.io.IOException;
import java.util.List;

import static TemplateMethodPattern.common.Utiles.printGroups;

public class Main {
    public static void main(String[] args) throws IOException {
        GroupingStrategy groupingStrategy = new JobTitleBasedGroupingStrategy();

        List<Student> students = ReadStudents.fromFile("student.data");
        List<Group> groups = groupingStrategy.group(students);

        printGroups(groups);
    }
}
