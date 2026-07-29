package v5.app;

import v5.app.states.*;
import v5.bot.Bot;
import v5.domain.Community;
import v5.domain.Member;
import v5.domain.events.*;
import v5.events.Event;
import v5.fsm.*;

import java.util.Arrays;

public class WaterballBot extends Bot {
    private final Member self;

    public WaterballBot(Community community, Member self) {
        super(buildMachine(community, self));
        this.self = self;
    }

    /**
     * A command is message, whose content match specific command name, tagged at the bot.
     * check is command to the bot
     */
    private static boolean isCommand(Event e, String name, Member self){
        return e instanceof NewMessageEvent m &&
                m.message().content().equals(name) &&
                Arrays.asList(m.message().tags()).contains(self.userId());
    }

    /**
     * build the finite state machine for bot
     * build inner composite state with from state, transition, guard, action, to state
     * transition, guard, action currently use lambda to declare in present.
     * from state, to state , all state currently reuse in all application.
     */
    private static StateMachine buildMachine(Community community, Member self) {
        // shared memory object
        RecordContext recordCtx = new RecordContext();
        KingContext kingCtx = new KingContext();

        //<editor-fold desc="bot inner composite state : normal flow">
        DefaultConversationState defaultConversation = new DefaultConversationState(community, self);
        InteractingState interacting = new InteractingState(community, self);

        StateMachine normalInner = new StateMachine(() -> community.onlineCount() >= 10 ? interacting : defaultConversation);

        normalInner.addTransition(new Transition(defaultConversation, LoginEvent.class, e-> community.onlineCount() >= 10, Action.NONE, interacting));
        normalInner.addTransition(new Transition(interacting, LogoutEvent.class, e-> community.onlineCount() < 10, Action.NONE, defaultConversation));

        CompositeState normal = new CompositeState(normalInner);
        //</editor-fold>

        //<editor-fold desc="bot inner composite state : record flow">
        WaitingState waiting = new WaitingState();
        RecordingState recording = new RecordingState(community, self, recordCtx);

        StateMachine recordInner = new StateMachine(() -> community.broadcast().isOnAir() ? recording : waiting);

        recordInner.addTransition(new Transition(waiting, GoBroadcastingEvent.class, Guard.ALWAYS, Action.NONE, recording));
        recordInner.addTransition(new Transition(waiting, GoBroadcastingEvent.class, Guard.ALWAYS, Action.NONE, waiting));

        CompositeState record = new CompositeState(recordInner);
        //</editor-fold>

        //<editor-fold desc="bot inner composite state : knowledgeKing flow">
        QuestioningState questioning = new QuestioningState(community, self, kingCtx, new QuestionBank());
        ThanksForJoiningState thanksForJoining = new ThanksForJoiningState(community, self, kingCtx);

        StateMachine knowledgeKingInner = new StateMachine(() -> questioning);

        knowledgeKingInner.addTransition(new Transition(questioning, NewMessageEvent.class, e -> questioning.isFinished(), Action.NONE, thanksForJoining));
        knowledgeKingInner.addTransition(new Transition(questioning, TimeElapsedEvent.class, e -> !community.clock().now().isBefore(kingCtx.questionDeadline), Action.NONE, thanksForJoining));
        knowledgeKingInner.addTransition(new Transition(thanksForJoining, NewMessageEvent.class,
                e -> isCommand(e, "play again", self) && community.quota().canAfford(5),
                e -> {
                    community.quota().consume(5);
                    community.sendMessage(self, "KnowledgeKing is gonna start again!", new String[0]);
                },
                questioning));

        CompositeState knowledgeKing = new CompositeState(knowledgeKingInner);
        //</editor-fold>

        //<editor-fold desc="bot root fsm : root flow">
        StateMachine root = new StateMachine(() -> normal);

        root.addTransition(new Transition(normal, NewMessageEvent.class,
                e-> isCommand(e, "record", self) && community.quota().canAfford(3),
                e -> {
                    community.quota().consume(3);
                    recordCtx.recorder = ((NewMessageEvent) e).creator(); //implicit change class to get creator
                },
                record));
        root.addTransition(new Transition(recording, NewMessageEvent.class,
                e-> isCommand(e, "stop-recording", self) && ((NewMessageEvent) e).creator() == recordCtx.recorder,
                Action.NONE,
                normal));
        root.addTransition(new Transition(normal, NewMessageEvent.class,
                e-> isCommand(e, "king", self) && ((NewMessageEvent) e).creator().isAdmin() && community.quota().canAfford(5),
                e -> {
                    community.quota().consume(5);
                    community.sendMessage(self, "KnowledgeKing is started!", new String[0]);
                },
                knowledgeKing));
        root.addTransition(new Transition(knowledgeKing, NewMessageEvent.class,
                e-> isCommand(e, "king-stop", self) && ((NewMessageEvent) e).creator().isAdmin(),
                e -> kingCtx.clear(),
                normal));
        root.addTransition(new Transition(knowledgeKing, NewMessageEvent.class,
                e-> kingCtx.thanksDeadline != null && !community.clock().now().isBefore(kingCtx.thanksDeadline),
                e -> kingCtx.clear(),
                normal));
        //</editor-fold>

        return root;
    }

    /**
     * bot should ignore the event cause by itself, so subclass should implement this method to check if the event is caused by bot itself.
     */
    @Override
    protected boolean isMyOwn(Event e) {
        return e instanceof AuthoredEvent a && a.creator() == self;
    }
}
