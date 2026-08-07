package domain;

import domain.events.BroadcastStoppedEvent;
import domain.events.GoBroadcastingEvent;
import domain.events.LoginEvent;
import domain.events.LogoutEvent;
import domain.events.NewMessageEvent;
import domain.events.NewPostEvent;
import domain.events.SpeakEvent;
import domain.events.TimeElapsedEvent;
import events.Event;
import events.EventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Aggregate root & Mediator of the world.
 *
 * INVARIANT (every mutation method): mutate → echo → notify, in that order.
 * Ids die at the door: {@link #findOnlineMember(String)} resolves them once,
 * everything below works with Member objects. Whoever mutates, prints.
 */
public class Community {
    private final Consumer<String> printer; //System.out.println(String) has a matching shape
    /** Insertion-ordered = login-ordered; the bot registers first. */
    private final Map<String, Member> onlineMembers = new LinkedHashMap<>();
    private final ChatRoom chatRoom = new ChatRoom();
    private final Broadcast broadcast = new Broadcast();
    private final Forum forum = new Forum();
    private final Clock clock;
    private Quota quota = new Quota(0);
    private final List<EventListener> listeners = new ArrayList<>();
    private int loginCounter = 0;

    public Community() {
        this(System.out::println); //call Community(Consumer<String> printer)
    }

    public Community(Consumer<String> printer) {
        this.printer = printer;
        this.clock = new Clock(printer);
    }

    // ---- wiring (composition root only) ------------------------------------

    public void addListener(EventListener l) {
        listeners.add(l);
    }

    /** Bots occupy an online slot (online count INCLUDES the bot). */
    public Member registerBot(String botId) {
        Member bot = new Member(botId, false, true, loginCounter++);
        onlineMembers.put(botId, bot);
        return bot;
    }

    // ---- lifecycle ----------------------------------------------------------

    public void start(LocalDateTime time, int quota) {
        this.clock.set(time);
        this.quota = new Quota(quota);
    }

    // ---- mutations (mutate → echo → notify) ---------------------------------

    public void login(String userId, boolean isAdmin) {
        Member member = new Member(userId, isAdmin, false, loginCounter++);
        onlineMembers.put(userId, member);
        // TODO(spec): no login echo documented in DESIGN.md — verify against .out files.
        notifyListeners(new LoginEvent(member));
    }

    public void logout(String userId) {
        Member member = findOnlineMember(userId);
        onlineMembers.remove(userId);
        // TODO(spec): no logout echo documented in DESIGN.md — verify against .out files.
        notifyListeners(new LogoutEvent(member));
    }

    public void sendMessage(String authorId, String content, String[] tags) {
        sendMessage(findOnlineMember(authorId), content, tags);
    }

    /** Overload so the bot can act with its own Member object. */
    public void sendMessage(Member author, String content, String[] tags) {
        Message message = new Message(author, content, tags);
        chatRoom.add(message);
        printer.accept(author.chatPrefix() + ": " + content + renderTags(tags));
        notifyListeners(new NewMessageEvent(message));
    }

    public void newPost(String authorId, String id, String title, String content, String[] tags) {
        Member author = findOnlineMember(authorId);
        Post post = new Post(id, title, content, tags, author);
        forum.add(post);
        printer.accept(author.userId() + ": 【" + title + "】" + content + renderTags(tags));
        notifyListeners(new NewPostEvent(post));
    }

    public void addComment(String postId, String authorId, String content) {
        addComment(postId, findOnlineMember(authorId), content);
    }

    public void addComment(String postId, Member author, String content) {
        Post post = forum.findPost(postId);
        post.addComment(new Comment(author, content));
        printer.accept(author.chatPrefix() + " comment in post " + postId + ": " + content);
        // No comment event in the shared kernel (DESIGN.md lists eight domain events).
    }

    public void goBroadcasting(String speakerId) {
        goBroadcasting(findOnlineMember(speakerId));
    }

    public void goBroadcasting(Member speaker) {
        broadcast.setCurrentSpeaker(speaker);
        printer.accept(speaker.isBot()
                ? "🤖 go broadcasting..."
                : "📢 " + speaker.userId() + " is broadcasting...");
        notifyListeners(new GoBroadcastingEvent(speaker));
    }

    public void speak(String speakerId, String content) {
        speak(findOnlineMember(speakerId), content);
    }

    public void speak(Member speaker, String content) {
        Speak speak = new Speak(speaker, content);
        broadcast.addSpeak(speak);
        printer.accept(speaker.isBot()
                ? "🤖 speaking: " + content
                : "📢 " + speaker.userId() + ": " + content);
        notifyListeners(new SpeakEvent(speak));
    }

    public void stopBroadcasting(String speakerId) {
        stopBroadcasting(findOnlineMember(speakerId));
    }

    public void stopBroadcasting(Member speaker) {
        broadcast.setCurrentSpeaker(null);
        printer.accept(speaker.isBot()
                ? "🤖 stop broadcasting..."
                : "📢 " + speaker.userId() + " stop broadcasting");
        notifyListeners(new BroadcastStoppedEvent(speaker));
    }

    public void elapse(int amount, String unit) {
        clock.elapse(amount, unit); // Clock mutates and echoes ("🕑 5 seconds elapsed...")
        notifyListeners(new TimeElapsedEvent(amount, unit));
    }

    // ---- queries ------------------------------------------------------------

    public Member findOnlineMember(String userId) {
        Member member = onlineMembers.get(userId);
        if (member == null) {
            throw new IllegalArgumentException("No online member with id " + userId);
        }
        return member;
    }

    public List<Member> onlineMembers() {
        return List.copyOf(onlineMembers.values());
    }

    public int onlineCount() {
        return onlineMembers.size();
    }

    public ChatRoom chatRoom() {
        return chatRoom;
    }

    public Broadcast broadcast() {
        return broadcast;
    }

    public Forum forum() {
        return forum;
    }

    public Clock clock() {
        return clock;
    }

    public Quota quota() {
        return quota;
    }

    // -------------------------------------------------------------------------

    private void notifyListeners(Event e) {
        for (EventListener l : listeners) {
            l.onEvent(e);
        }
    }

    /** Tags render comma-separated after the content: " @1, @2, @4". */
    private static String renderTags(String[] tags) {
        if (tags == null || tags.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < tags.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append('@').append(tags[i]);
        }
        return sb.toString();
    }
}
