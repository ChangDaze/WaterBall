package TemplateMethodPattern.v2;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.GroupingStrategy;
import TemplateMethodPattern.common.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CutBasedGroupingStrategy implements GroupingStrategy {
    public static final int GROUP_MIN_SIZE = 6;

    @Override
    public List<Group> group(List<Student> students) {
        // 第一刀
        Map<Object, Group> firstCut = new HashMap<>();
        for (Student student : students) {
            Object key = cutBy(student);
            if (!firstCut.containsKey(key)) {
                firstCut.put(key, new Group());
            }
            firstCut.get(key).addStudent(student);
        }
        List<Group> firstCutGroups = new ArrayList<>(firstCut.values());

        // 第二刀:人數(6人一組)
        List<Group> secondCutGroups = new ArrayList<>();
        for (Group group : firstCutGroups) {
            secondCutGroups.addAll(group.splitBySize(GROUP_MIN_SIZE));
        }

        // 將不足 6 人的組別併到人滿的組別
        List<Group> nonFullGroups = new ArrayList<>();
        List<Group> fullGroups = new ArrayList<>();
        for (Group group : secondCutGroups) {
            if (group.size() < GROUP_MIN_SIZE) {
                nonFullGroups.add(group);
            } else {
                fullGroups.add(group);
            }
        }

        //併組
        for (Group nonFullGroup : nonFullGroups) {
            for (Group fullGroup : fullGroups) {
                if (meetMergeCriteria(nonFullGroup, fullGroup)) {
                    System.out.printf("Merge group (%d) to (%d). \n", nonFullGroup.getGroupNo(), fullGroup.getGroupNo());
                    fullGroup.merge(nonFullGroup);
                    break;
                }
            }
        }

        return fullGroups;
    }

    /**
     * 取出分組時使用的Key
     */
    protected abstract Object cutBy(Student student);

    /**
     * 兩個Group條件是否符合合併條件
     */
    protected boolean meetMergeCriteria(Group nonFullGroup, Group fullGroup){
        return true;
    }
}
