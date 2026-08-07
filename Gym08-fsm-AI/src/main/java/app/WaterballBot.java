package app;

import bot.Bot;
import domain.Community;
import domain.Member;
import domain.events.AuthoredEvent;
import domain.events.BroadcastStoppedEvent;
import domain.events.GoBroadcastingEvent;
import domain.events.LoginEvent;
import domain.events.LogoutEvent;
import domain.events.NewMessageEvent;
import domain.events.TimeElapsedEvent;
import events.Event;
import fsm.Action;
import fsm.Guard;
import fsm.StateMachine;
import fsm.Transition;
import fsm.composite.CompositeState;

import java.util.Arrays;

/**
 * The Waterball community bot. Three CompositeState INSTANCES (normal /
 * record / king — configuration, not subclasses) around a root machine;
 * every guard/action is a lambda closing over the domain objects.
 */
public class WaterballBot extends Bot {
    private final Member self;

    public WaterballBot(Community community, Member self) {
        super(buildMachine(community, self));
        this.self = self;
    }

    @Override
    protected boolean isMyOwn(Event e) {
        return e instanceof AuthoredEvent a && a.creator() == self;
    }

    private static StateMachine buildMachine(Community community, Member self) {
        RecordContext recordCtx = new RecordContext();
        KingContext kingCtx = new KingContext();

        // --- normal composite: Default ⇄ Interacting on crowd size (bot counts) ---
        DefaultConversationState defaultConversation = new DefaultConversationState(community, self);
        InteractingState interacting = new InteractingState(community, self);
        StateMachine normalInner = new StateMachine(
                () -> community.onlineCount() >= 10 ? interacting : defaultConversation);
        normalInner.addTransition(new Transition(defaultConversation, LoginEvent.class,
                e -> community.onlineCount() >= 10, Action.NONE, interacting));
        normalInner.addTransition(new Transition(interacting, LogoutEvent.class,
                e -> community.onlineCount() < 10, Action.NONE, defaultConversation));
        CompositeState normal = new CompositeState(normalInner);

        // --- record composite: Waiting ⇄ Recording follows the broadcast ---------
        WaitingState waiting = new WaitingState();
        RecordingState recording = new RecordingState(community, self, recordCtx);
        StateMachine recordInner = new StateMachine(
                () -> community.broadcast().isOnAir() ? recording : waiting);
        recordInner.addTransition(new Transition(waiting, GoBroadcastingEvent.class,
                Guard.ALWAYS, Action.NONE, recording));
        recordInner.addTransition(new Transition(recording, BroadcastStoppedEvent.class,
                Guard.ALWAYS, Action.NONE, waiting));
        CompositeState record = new CompositeState(recordInner);

        // --- king composite: Questioning → Thanks → (play again) -----------------
        QuestioningState questioning = new QuestioningState(community, self, kingCtx, new QuestionBank());
        ThanksForJoiningState thanks = new ThanksForJoiningState(community, self, kingCtx);
        StateMachine kingInner = new StateMachine(() -> questioning);
        kingInner.addTransition(new Transition(questioning, NewMessageEvent.class,
                e -> questioning.isFinished(), Action.NONE, thanks));
        kingInner.addTransition(new Transition(questioning, TimeElapsedEvent.class,
                e -> !community.clock().now().isBefore(kingCtx.questionDeadline), Action.NONE, thanks));
        kingInner.addTransition(new Transition(thanks, NewMessageEvent.class,
                e -> isCommand(e, "play again", self) && community.quota().canAfford(5),
                e -> {
                    community.quota().consume(5);
                    community.sendMessage(self, "KnowledgeKing is gonna start again!", new String[0]);
                }, questioning));
        CompositeState king = new CompositeState(kingInner);

        // --- root machine ---------------------------------------------------------
        StateMachine root = new StateMachine(() -> normal);
        root.addTransition(new Transition(normal, NewMessageEvent.class,
                e -> isCommand(e, "record", self) && community.quota().canAfford(3),
                e -> {
                    community.quota().consume(3);
                    recordCtx.recorder = ((NewMessageEvent) e).creator();
                }, record));
        root.addTransition(new Transition(record, NewMessageEvent.class,
                e -> isCommand(e, "stop-recording", self)
                        && ((NewMessageEvent) e).creator() == recordCtx.recorder,
                Action.NONE, normal)); // replay flushes in RecordingState.onExit
        root.addTransition(new Transition(normal, NewMessageEvent.class,
                e -> isCommand(e, "king", self) && ((NewMessageEvent) e).creator().isAdmin()
                        && community.quota().canAfford(5),
                e -> {
                    community.quota().consume(5);
                    community.sendMessage(self, "KnowledgeKing is started!", new String[0]);
                }, king));
        // TODO(spec): king-stop permission assumed admin-only and output assumed silent — verify.
        root.addTransition(new Transition(king, NewMessageEvent.class,
                e -> isCommand(e, "king-stop", self) && ((NewMessageEvent) e).creator().isAdmin(),
                e -> kingCtx.clear(), normal));
        root.addTransition(new Transition(king, TimeElapsedEvent.class,
                e -> kingCtx.thanksDeadline != null
                        && !community.clock().now().isBefore(kingCtx.thanksDeadline),
                e -> kingCtx.clear(), normal));
        return root;
    }

    /** A command is a message whose content IS the command name, tagged at the bot. */
    private static boolean isCommand(Event e, String name, Member self) {
        return e instanceof NewMessageEvent m
                && m.message().content().equals(name)
                && Arrays.asList(m.message().tags()).contains(self.userId());
    }
}
