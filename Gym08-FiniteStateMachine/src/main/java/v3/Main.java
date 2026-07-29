package v3;

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
