package TemplateMethodPattern.v2;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.GroupingStrategy;
import TemplateMethodPattern.common.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class TimeSlotsBasedGroupStrategy extends CutBasedGroupingStrategy {
    @Override
    protected Object cutBy(Student student) {
        return hashTimeSlots(student);
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
