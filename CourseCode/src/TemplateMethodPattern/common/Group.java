package TemplateMethodPattern.common;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private static int count = 0; //物件編號計數器
    private final int groupNo;
    private final List<Student> students;

    /**
     * 做出空Group
     */
    public Group() { this(new ArrayList<>()); }

    public Group(List<Student> students) {
        this.groupNo = ++count; //應該是編號
        this.students = new ArrayList<>(students);
    }

    public Student get(int index) {return  students.get(index);}

    public void addStudent(Student student) {students.add(student);}

    public List<Group> splitBySize(int groupSize) {
        List<Group> groups = new ArrayList<>();
        for (int i=0; i < size(); i+= groupSize) {
            groups.add(new Group(students.subList(i, Math.min(i + groupSize, students.size()))));
        }
        return groups;
    }

    public void merge(Group group) {students.addAll(group.getStudents());}

    public List<Student> getStudents() {return students; }

    public int size() {return students.size();}

    public int getGroupNo() {return groupNo;}
}
