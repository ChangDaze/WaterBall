package v3;

import ExampleFSM.C1StateMachine;
import FSMv2.*;

import java.util.ArrayList;
import java.util.List;

public class RobotSettingSystem {
    public Robot createRobot(Community community, ExtraRobotState[] states){
        // Transitions inside Robot
        List<Transition> robotTransitions = new ArrayList<Transition>();


        // Build leaf states for Normal
        State defaultConversation = new DefaultConversation(community);
        State interacting = new Interacting(community);

        // Transitions inside Normal
        List<Transition> normalTransitions = new ArrayList<Transition>();
        normalTransitions.add(new Transition(interacting, new ToInteracting(community)));
        normalTransitions.add(new Transition(defaultConversation, new ToDefaultConversation(community)));

        // Create Normal state machine
        State normal = new Normal(defaultConversation, normalTransitions);

        for (ExtraRobotState state : states) {
            switch (state) {
                case Record:
                    // Build leaf states for Record
                    State waiting = new Waiting(community);
                    State recording = new Recording(community);

                    // Transitions inside Record
                    List<Transition> recordTransitions = new ArrayList<Transition>();
                    recordTransitions.add(new Transition(recording, new ToRecording(community)));

                    // Create Record state machine
                    State record = new Record(waiting, recordTransitions);

                    // add more robot transitions
                    robotTransitions.add(new Transition(normal, new StopRecording(community)));
                    robotTransitions.add(new Transition(record, new ToRecord(community)));
                    break;
                case KnowledgeKing:
                    // Build leaf states for KnowledgeKing
                    State questioning = new Questioning(community);
                    State thanksForJoining = new ThanksForJoining(community);

                    // Transitions inside KnowledgeKing
                    List<Transition> knowledgeKingTransitions = new ArrayList<Transition>();
                    knowledgeKingTransitions.add(new Transition(thanksForJoining, new ToThanksForJoining(community)));

                    // Create KnowledgeKing state machine
                    State knowledgeKing = new KnowledgeKing(questioning, knowledgeKingTransitions);

                    // add more robot transitions
                    robotTransitions.add(new Transition(normal, new KingStop(community)));
                    robotTransitions.add(new Transition(normal, new ThanksForJoiningEnd(community)));
                    robotTransitions.add(new Transition(knowledgeKing, new ToKnowledgeKing(community)));
                    break;
                default:
                    break;
            }
        }

        // Create Robot state machine
        Robot robot = new Robot(normal, robotTransitions);

        return robot;
    }
}
