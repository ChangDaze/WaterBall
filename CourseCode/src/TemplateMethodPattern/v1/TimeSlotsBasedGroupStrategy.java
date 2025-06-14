package TemplateMethodPattern.v1;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.GroupingStrategy;
import TemplateMethodPattern.common.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class TimeSlotsBasedGroupStrategy implements GroupingStrategy {
    public static final int GROUP_MIN_SIZE = 6;

    @Override
    public List<Group> group(List<Student> students) {
        // 第一刀:程式語言專長
        // 用HashMap將相同語言的Student分到一起的Group，再轉成List (在C#可能不用特別轉， 因為都有實作IEnumerable)
        Map<String, Group> firstCut = new HashMap<>();
        for (Student student : students) {
            String timeSlotsHash = hashTimeSlots(student);
            if (!firstCut.containsKey(timeSlotsHash)) {
                firstCut.put(timeSlotsHash, new Group());
            }
            firstCut.get(timeSlotsHash).addStudent(student);
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

        //這方法第二刀下去nonFullGroups都只會加到for迴圈的第一組，可以依情境優化
        for (Group nonFullGroup : nonFullGroups) {
            for (Group fullGroup : fullGroups) {
                // 不足的補到任意滿組的裡面
                System.out.printf("Merge group (%d) to (%d). \n", nonFullGroup.getGroupNo(), fullGroup.getGroupNo());
                fullGroup.merge(nonFullGroup);
                break;
            }
        }

        return fullGroups;
    }

    /**
     * 將空閒的連續時間段hash成4個boolean int
     * 9~12, 13~16, 17~19, 20~21 => 1111 or 1010 ...
     */
    private String hashTimeSlots(Student student){
        boolean[] slots = student.getAvailableTimeSlots();
        return format(
            "%d%d%d%d",
            convertBooleanToNumber(slots[0] && slots[1] && slots[2] && slots[3]),
            convertBooleanToNumber(slots[4] && slots[5] && slots[6] && slots[7]),
            convertBooleanToNumber(slots[8] && slots[9] && slots[10]),
            convertBooleanToNumber(slots[11] && slots[12])
        );
    }

    /**
     * 將bool轉換成用int表示
     */
    private int convertBooleanToNumber(boolean bool){
        return bool ? 1 : 0;
    }
}
