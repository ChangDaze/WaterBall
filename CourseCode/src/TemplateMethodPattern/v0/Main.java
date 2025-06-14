package TemplateMethodPattern.v0;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.ReadStudents;
import TemplateMethodPattern.common.Student;

import java.io.IOException;
import java.util.List;

import static TemplateMethodPattern.common.Utiles.printGroups;

public class Main {
    public static void main(String[] args) throws IOException {
        //System.out.println(System.getProperty("user.dir"));
        LanguageBasedGroupingStrategy groupingStrategy = new LanguageBasedGroupingStrategy();

        List<Student> students = ReadStudents.fromFile("student.data");
        List<Group> groups = groupingStrategy.group(students);

        printGroups(groups);
    }
}
