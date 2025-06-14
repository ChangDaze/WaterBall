package TemplateMethodPattern.v1;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.GroupingStrategy;
import TemplateMethodPattern.common.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageBasedGroupingStrategy implements GroupingStrategy {
    public static final int GROUP_MIN_SIZE = 6;

    @Override
    public List<Group> group(List<Student> students) {
        // 第一刀:程式語言專長
        // 用HashMap將相同語言的Student分到一起的Group，再轉成List (在C#可能不用特別轉， 因為都有實作IEnumerable)
        Map<String, Group> firstCut = new HashMap<>();
        for (Student student : students) {
            if (!firstCut.containsKey(student.getLanguage())) {
                firstCut.put(student.getLanguage(), new Group());
            }
            firstCut.get(student.getLanguage()).addStudent(student);
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

        //這方法第二刀下去剛好最後剩下的人數會剛好不足一組，不然會都加到前面幾組
        //nonFullGroups理論上非常少組
        for (Group nonFullGroup : nonFullGroups) {
            for (Group fullGroup : fullGroups) {
                // 如果兩個組別使用的程式語言一樣，才併組
                if (fullGroup.get(0).getLanguage().equals(nonFullGroup.get(0).getLanguage())) {
                    System.out.printf("Merge group (%d) to (%d). \n", nonFullGroup.getGroupNo(), fullGroup.getGroupNo());
                    fullGroup.merge(nonFullGroup);
                    break;
                }
            }
        }

        return fullGroups;
    }
}
