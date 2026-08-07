package v5.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import v5.domain.Community;
import v5.domain.Member;
import v5.domain.Quota;
import v5.domain.events.LoginEvent;
import v5.domain.events.NewMessageEvent;
import v5.domain.events.TimeElapsedEvent;
import v5.events.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommunityTest {
    final List<String> log = new ArrayList<>();
    final Community community = new Community(s -> log.add("OUT " + s));
    final List<Event> received = new ArrayList<>();

    @BeforeEach
    void listen(){
        community.addListener(e ->{
            received.add(e);
            log.add("EVT " + e.getClass().getSimpleName());
        });
        community.start(LocalDateTime.of(2023, 1, 1, 0, 0), 20);
    }

    @Test
    void mutate_echo_notify_in_that_order() {
        community.login("3", false);
        log.clear();
        community.sendMessage("3", "record", new String[]{"bot"});
        assertEquals(List.of("OUT 💬 3: record @bot", "EVT NewMessageEvent"), log);
        assertEquals(1, community.chatRoom().messages().size(), "mutation must precede echo");
    }

    @Test
    void ids_die_at_the_door_same_member_object_everywhere(){
        community.login("3", false);
        Member member = community.findOnlineMember("3");
        community.sendMessage("3", "hi", new String[0]);
        NewMessageEvent event = (NewMessageEvent) received.stream()
                .filter(e -> e instanceof NewMessageEvent)
                .findFirst().orElseThrow();

        assertSame(member, event.message().author());
        assertSame(member, ((LoginEvent) received.getFirst()).member());
    }

    @Test
    void broadcast_echo_formats() {
        community.login("4", false);
        log.clear();
        community.goBroadcasting("4");
        community.speak("4", "Test");
        community.stopBroadcasting("4");
        assertEquals(List.of(
                "OUT 📢 4 is broadcasting...",
                "EVT GoBroadcastingEvent",
                "OUT 📢 4: Test",
                "EVT SpeakEvent",
                "OUT 📢 4 stop broadcasting",
                "EVT BroadcastStoppedEvent"
        ), log);
        assertFalse(community.broadcast().isOnAir());
    }

    @Test
    void message_tags_render_comma_separated(){
        community.login("6", false);
        log.clear();
        community.sendMessage("6", "hi", new String[]{"1", "2", "4"});
        assertEquals("OUT 💬 6: hi @1, @2, @4", log.getFirst());
    }

    @Test
    void new_post_echo_has_no_chat_prefix(){
        community.login("2", false);
        log.clear();
        community.newPost("2", "101", "Hello Forum", "This is my first post", new String[0]);
        assertEquals(List.of("OUT 2: 【Hello Forum】This is my first post", "EVT NewPostEvent"), log);
    }

    @Test
    void elapse_echoes_amount_and_unit_verbatim_and_moves_the_clock(){
        log.clear();
        community.elapse(5, "seconds");
        assertEquals(List.of("OUT 🕑 5 seconds elapsed...", "EVT TimeElapsedEvent"), log);
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0, 5), community.clock().now());
        TimeElapsedEvent e = (TimeElapsedEvent) received.getLast();
        assertEquals(5, e.amount());
        assertEquals("seconds", e.unit());
    }

    @Test
    void quota_is_one_shared_pool_of_costs() {
        Quota quota = community.quota();
        assertTrue(quota.canAfford(5));
        quota.consume(5);
        quota.consume(5);
        quota.consume(5);
        assertEquals(5, quota.remaining());
        quota.consume(3);
        assertEquals(2, quota.remaining());
        assertFalse(quota.canAfford(3));
        assertFalse(quota.canAfford(5));
    }

    @Test
    void online_count_includes_bot_and_keeps_login_order() {
        Member bot = community.registerBot("bot");
        community.login("1", false);
        community.login("2", true);
        assertEquals(3, community.onlineCount());
        assertEquals(List.of(bot, community.findOnlineMember("1"), community.findOnlineMember("2")),
                community.onlineMembers());
        community.logout("1");
        assertEquals(2, community.onlineCount());
        assertThrows(IllegalArgumentException.class, () -> community.findOnlineMember("1"));
    }

    @Test
    void comments_echo_with_post_id() {
        community.login("2", false);
        community.newPost("2", "101", "title", "content", new String[0]);
        log.clear();
        community.addComment("101", "2", "great");
        assertEquals(List.of("OUT 💬 2 comment in post 101: great"), log);
        assertEquals(1, community.forum().findPost("101").comments().size());
    }
}






