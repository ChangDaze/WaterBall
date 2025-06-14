package TemplateMethodPattern.v2;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.GroupingStrategy;
import TemplateMethodPattern.common.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageBasedGroupingStrategy extends CutBasedGroupingStrategy {

    @Override
    protected Object cutBy(Student student) {
        return student.getLanguage();
    }

    @Override
    protected boolean meetMergeCriteria(Group nonFullGroup, Group fullGroup){
        return nonFullGroup.get(0).getLanguage().equals(fullGroup.get(0).getLanguage());
    }
}
