import ExampleFSM.C21toC22Trigger;
import ExampleFSM.C22toC21Trigger;
import FSM.Transition;
import v3.Community;
import v3.ExtraRobotState;
import v3.Robot;
import v3.RobotSettingSystem;

import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        Community community = new Community();
        RobotSettingSystem robotSettingSystem = new RobotSettingSystem();
        Robot robot = robotSettingSystem.createRobot(community, new ExtraRobotState[]{
                ExtraRobotState.Record,
                ExtraRobotState.KnowledgeKing
        });
    }
}
