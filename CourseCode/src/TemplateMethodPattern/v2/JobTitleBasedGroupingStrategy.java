package TemplateMethodPattern.v2;

import TemplateMethodPattern.common.Group;
import TemplateMethodPattern.common.Student;

public class JobTitleBasedGroupingStrategy extends CutBasedGroupingStrategy{

    @Override
    protected Object cutBy(Student student) {
        return student.getJobTitle();
    }

    @Override
    protected boolean meetMergeCriteria(Group nonFullGroup, Group fullGroup){
        return nonFullGroup.get(0).getJobTitle().equals(fullGroup.get(0).getJobTitle());
    }
}
