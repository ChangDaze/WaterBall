package v5.domain;

import v5.domain.events.*;
import v5.events.Event;
import v5.events.EventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Community {
    private final Consumer<String> printer; //Is @FunctionalInterface, offer void accept(T t); method
    private final Map<String, Member> onlineMembers = new LinkedHashMap<>(); //LinkedHashMap is base on hashmap and add another linked list relation on hashmap, so have both hash search and linked list order ability, currently is for login order convenience.
    private final ChatRoom chatRoom = new ChatRoom();
    private final Broadcast broadcast = new Broadcast();
    private final Forum forum = new Forum();
    private final Clock clock;
    private Quota quota = new Quota(0); //access control is outside the community, let community pure to do input(mutate), output(react), trigger events(notify observers), so in community no one use quota.
    private final List<EventListener> listeners = new ArrayList<>();
    private int loginCount = 0; //for the login order use, so only add up, and separate from userid.

    public Community(){
        //the println would be wrapped into Consumer<String> interface and generate a class when compile
        //Consumer<T>'s T decide sent what to println, in this case is String, and println meet the signature with only String
        //println bound to the System.out, so when void accept(String s) be called, System.out will be called too.
        this(System.out::println);
    }

    public Community(Consumer<String> printer){
        this.printer = printer;
        this.clock = new Clock(printer);
    }

    /**
     * currently also offer the ability for bot, separate from registerBot to decouple function for future use.
     */
    public void addListener(EventListener l) {
        listeners.add(l);
    }

    /**
     * notify the observers.
     */
    private void notifyListeners(Event e) {
        for (EventListener l : listeners) {
            l.onEvent(e);
        }
    }

    /**
     * Bot not trigger the login event and need tag with bot so need a special register.
     */
    public Member registerBot(String botId){
        Member bot = new Member(botId, false, true, loginCount++);
        onlineMembers.put(botId, bot);
        return bot;
    }

    /**
     * user id to get member, can offer the ability to use tage to find object.
     */
    public Member findOnlineMember(String userId) {
        Member member = onlineMembers.get(userId);
        if (member == null) {
            throw new IllegalArgumentException("No online member with id " + userId);
        }
        return member;
    }

    /**
     * just a usual use share function to render the tags into a string for output.
     */
    private static String renderTags(String[] tags) {
        if(tags == null || tags.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" "); //beging with space to separate the tags.
        for (int i = 0; i < tags.length; i++) {
            if(i > 0) {
                sb.append(", ");
            }
            sb.append('@').append(tags[i]);
        }
        return sb.toString();
    }

    /**
     * signature to meet the spec.
     */
    public void start(LocalDateTime time, int quota) {
        this.clock.set(time);
        this.quota = new Quota(quota);
    }

    /**
     * login and trigger login event to notify the observers.
     */
    public void login(String userId, boolean isAdmin) {
        Member member = new Member(userId, isAdmin, false, loginCount++);
        onlineMembers.put(userId, member);
        notifyListeners(new LoginEvent(member));
    }

    /**
     * logout and trigger logout event to notify the observers.
     */
    public void logout(String userId) {
        Member member = findOnlineMember(userId);
        onlineMembers.remove(userId);
        notifyListeners(new LogoutEvent(member));
    }

    /**
     * send message to chatroom, print output and trigger new message event to notify the observers.
     */
    public void sendMessage(Member author, String content, String[] tags) {
        Message message = new Message(author, content, tags);
        chatRoom.add(message);
        printer.accept(author.chatPrefix() + ": " + content + renderTags(tags));
        notifyListeners(new NewMessageEvent(message));
    }

    /**
     * offer ability to use userid trigger send message.
     * like bot don't know the member object directly send message.
     */
    public void sendMessage(String userId, String content, String[] tags) {
        sendMessage(findOnlineMember(userId), content, tags);
    }

    /**
     * create a new post and add to forum, print output and trigger new post event to notify the observers.
     */
    public void newPost(String authorId, String id, String title, String content, String[] tags) {
        Member author = findOnlineMember(authorId);
        Post post = new Post(id, title, content, tags, author);
        forum.addPost(post);
        printer.accept(author.userId() + ": 【" + title + "】" + content + renderTags(tags));
        notifyListeners(new NewPostEvent(post));
    }

    /**
     * add comment to a post, print output, currently not trigger event for comment.
     */
    public void addComment(String postId, Member author, String content) {
        Post post = forum.findPost(postId);
        post.addComment(new Comment(author, content));
        printer.accept(author.chatPrefix() + " comment in post " + postId + ": " + content);
    }

    /**
     * offer ability to use userid trigger add comment.
     * like bot don't know the member object directly add comment.
     */
    public void addComment(String postId, String authorId, String content) {
        addComment(postId, findOnlineMember(authorId), content);
    }

    /**
     * trigger go broadcasting event to notify the observers.
     */
    public void goBroadcasting(Member speaker) {
        broadcast.setCurrentSpeaker(speaker);
        printer.accept(speaker.isBot()
                ? "🤖 go broadcasting..."
                : "📢 " + speaker.userId() + " is broadcasting...");
        notifyListeners(new GoBroadcastingEvent(speaker));
    }

    /**
     * offer ability to use userid trigger go broadcasting.
     * like bot don't know the member object directly go broadcasting.
     */
    public void goBroadcasting(String speakerId) {
        goBroadcasting(findOnlineMember(speakerId));
    }

    /**
     * trigger stop broadcasting event to notify the observers.
     */
    public  void speak(Member speaker, String content) {
        Speak speak = new Speak(speaker, content);
        broadcast.addSpeak(speak);
        printer.accept(speaker.isBot()
                ? "🤖 speaking: " + content
                : "📢 " + speaker.userId() + ": " + content);
        notifyListeners(new SpeakEvent(speak));
    }

    /**
     * offer ability to use userid trigger speak.
     * like bot don't know the member object directly speak.
     */
    public void speak(String speakerId, String content) {
        speak(findOnlineMember(speakerId), content);
    }

    /**
     * trigger stop broadcasting event to notify the observers.
     */
    public void stopBroadcasting(Member speaker) {
        broadcast.setCurrentSpeaker(null);
        printer.accept(speaker.isBot()
                ? "🤖 stop broadcasting..."
                : "📢 " + speaker.userId() + " stop broadcasting");
        notifyListeners(new BroadcastStoppedEvent(speaker));
    }

    /**
     * offer ability to use userid trigger stop broadcasting.
     * like bot don't know the member object directly stop broadcasting.
     */
    public void stopBroadcasting(String speakerId) {
        stopBroadcasting(findOnlineMember(speakerId));
    }

    /**
     * trigger time elapse event to notify the observers.
     */
    public void elapse(int amount, String unit) {
        clock.elapse(amount, unit);
        notifyListeners(new TimeElapsedEvent(amount, unit));
    }

    /**
     * just for bot state judgement.
     */
    public int onlineCount()
    {
        return onlineMembers.size();
    }

    /**
     * only get the list, don't have ability to directly change online members.
     */
    public List<Member> onlineMembers() {
        return List.copyOf(onlineMembers.values());
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
}
