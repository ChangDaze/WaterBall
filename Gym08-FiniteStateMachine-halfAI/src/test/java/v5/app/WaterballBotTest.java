package v5.app;

import org.junit.jupiter.api.Test;
import v5.app.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WaterballBotTest {
    private List<String> run(String... lines) {
        List<String> out = new ArrayList<>();
        Main.run(Stream.of(lines), out::add);
        return out;
    }

    static final String START = "[started] {\"time\": \"2023-08-07 00:00:00\", \"quota\": 20}";
    static final String Q0 = "🤖: 0. 請問哪個 SQL 語句用於選擇所有的行？\nA) SELECT *\nB) SELECT ALL\nC) SELECT ROWS\nD) SELECT DATA";

    @Test
    void default_conversation_carousel_replies_and_wraps() {
        List<String> out = run(START,
                "[login] {\"userId\": \"1\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"1\", \"content\": \"hello\", \"tags\": []}",
                "[new message] {\"authorId\": \"1\", \"content\": \"hello\", \"tags\": []}",
                "[new message] {\"authorId\": \"1\", \"content\": \"hello\", \"tags\": []}",
                "[new message] {\"authorId\": \"1\", \"content\": \"hello\", \"tags\": []}");
        assertEquals(List.of(
                "💬 1: hello", "🤖: good to hear @1",
                "💬 1: hello", "🤖: thank you @1",
                "💬 1: hello", "🤖: How are you @1",
                "💬 1: hello", "🤖: good to hear @1"), out);
    }

    @Test
    void message_tags_render_comma_separated() {
        List<String> out = run(START,
                "[login] {\"userId\": \"1\", \"isAdmin\": false}",
                "[login] {\"userId\": \"6\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"6\", \"content\": \"hi\", \"tags\": [\"1\", \"2\", \"4\"]}");
        assertEquals("💬 6: hi @1, @2, @4", out.getFirst());
    }

    @Test
    void default_conversation_comments_nice_post_and_post_echo_format() {
        List<String> out = run(START,
                "[login] {\"userId\": \"2\", \"isAdmin\": false}",
                "[new post] {\"id\": \"101\", \"authorId\": \"2\", \"title\": \"Hello Forum\", \"content\": \"This is my first post\", \"tags\": []}");
        assertEquals(List.of(
                "2: 【Hello Forum】This is my first post",
                "🤖 comment in post 101: Nice post @2"), out);
    }

    @Test
    void tenth_login_switches_to_interacting_bot_counts() {
        List<String> lines = new ArrayList<>(List.of(START));
        for (int i = 1; i <= 9; i++) {
            lines.add("[login] {\"userId\": \"" + i + "\", \"isAdmin\": false}");   // bot + 9 humans = 10 online
        }
        lines.add("[new message] {\"authorId\": \"9\", \"content\": \"Hello everyone\", \"tags\": []}");
        List<String> out = run(lines.toArray(String[]::new));
        assertEquals(List.of("💬 9: Hello everyone", "🤖: Hi hi😁 @9"), out);
    }

    @Test
    void interacting_carousel_has_two_replies() {
        List<String> lines = new ArrayList<>(List.of(START));
        for (int i = 1; i <= 9; i++) {
            lines.add("[login] {\"userId\": \"" + i + "\", \"isAdmin\": false}");
        }
        lines.add("[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": []}");
        lines.add("[new message] {\"authorId\": \"1\", \"content\": \"B\", \"tags\": []}");
        lines.add("[new message] {\"authorId\": \"1\", \"content\": \"C\", \"tags\": []}");
        List<String> out = run(lines.toArray(String[]::new));
        assertEquals(List.of(
                "💬 1: A", "🤖: Hi hi😁 @1",
                "💬 1: B", "🤖: I like your idea! @1",
                "💬 1: C", "🤖: Hi hi😁 @1"), out);
    }

    @Test
    void interacting_rallies_everyone_via_comment_in_login_order() {
        List<String> lines = new ArrayList<>(List.of(START));
        for (int i = 1; i <= 10; i++) {
            lines.add("[login] {\"userId\": \"" + i + "\", \"isAdmin\": false}");
        }
        lines.add("[new post] {\"id\": \"201\", \"authorId\": \"10\", \"title\": \"t\", \"content\": \"c\", \"tags\": []}");
        List<String> out = run(lines.toArray(String[]::new));
        assertEquals(List.of(
                "10: 【t】c",
                "🤖 comment in post 201: How do you guys think about it? @bot, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10"), out);
    }

    @Test
    void record_replay_flushes_on_stop_recording() {
        List<String> out = run(START,
                "[login] {\"userId\": \"3\", \"isAdmin\": false}",
                "[login] {\"userId\": \"4\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"3\", \"content\": \"record\", \"tags\": [\"bot\"]}",
                "[go broadcasting] {\"speakerId\": \"4\"}",
                "[speak] {\"speakerId\": \"4\", \"content\": \"Line1\"}",
                "[speak] {\"speakerId\": \"4\", \"content\": \"Line2\"}",
                "[stop broadcasting] {\"speakerId\": \"4\"}");
        assertEquals("🤖: [Record Replay] Line1\nLine2 @3", out.getLast());
    }

    @Test
    void stop_recording_by_non_recorder_is_ignored() {
        List<String> out = run(START,
                "[login] {\"userId\": \"3\", \"isAdmin\": false}",
                "[login] {\"userId\": \"4\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"3\", \"content\": \"record\", \"tags\": [\"bot\"]}",
                "[go broadcasting] {\"speakerId\": \"3\"}",
                "[speak] {\"speakerId\": \"3\", \"content\": \"Record test.\"}",
                "[new message] {\"authorId\": \"4\", \"content\": \"stop-recording\", \"tags\": [\"bot\"]}");
        assertEquals("💬 4: stop-recording @bot", out.getLast(),
                "no reply, no replay — record mode continues");
    }

    @Test
    void record_denied_when_quota_exhausted_is_silent() {
        List<String> out = run("[started] {\"time\": \"2023-08-07 00:00:00\", \"quota\": 2}",
                "[login] {\"userId\": \"2\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"2\", \"content\": \"record\", \"tags\": [\"bot\"]}",
                "[go broadcasting] {\"speakerId\": \"2\"}",
                "[speak] {\"speakerId\": \"2\", \"content\": \"x\"}",
                "[stop broadcasting] {\"speakerId\": \"2\"}");
        assertTrue(out.stream().noneMatch(s -> s.contains("Record Replay")),
                "record must be silently denied without quota; got: " + out);
    }

    @Test
    void knowledge_king_full_round_winner_announced_by_bot_broadcast() {
        List<String> out = run(START,
                "[login] {\"userId\": \"admin\", \"isAdmin\": true}",
                "[login] {\"userId\": \"1\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"admin\", \"content\": \"king\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"C\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": [\"bot\"]}");
        assertTrue(out.contains("🤖: KnowledgeKing is started!"), "got: " + out);
        assertTrue(out.contains(Q0), "questions are numbered from 0; got: " + out);
        assertEquals(3, out.stream().filter("🤖: Congrats! you got the answer! @1"::equals).count(),
                "got: " + out);
        assertTrue(out.containsAll(List.of(
                        "🤖 go broadcasting...", "🤖 speaking: The winner is 1", "🤖 stop broadcasting...")),
                "got: " + out);
    }

    @Test
    void answers_must_tag_the_bot_and_wrong_answers_are_silent() {
        List<String> out = run(START,
                "[login] {\"userId\": \"admin\", \"isAdmin\": true}",
                "[login] {\"userId\": \"1\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"admin\", \"content\": \"king\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": []}",       // untagged → ignored
                "[new message] {\"authorId\": \"1\", \"content\": \"D\", \"tags\": [\"bot\"]}"); // wrong → silent
        assertTrue(out.stream().noneMatch(s -> s.contains("Congrats")), "got: " + out);
    }

    @Test
    void question_timeout_announces_tie_then_20s_returns_to_normal_with_fresh_carousel() {
        List<String> out = run(START,
                "[login] {\"userId\": \"1\", \"isAdmin\": true}",
                "[new message] {\"authorId\": \"1\", \"content\": \"king\", \"tags\": [\"bot\"]}",
                "[1 hours elapsed]",                              // question deadline → Thanks
                "[20 seconds elapsed]",                           // thanks deadline → normal
                "[new message] {\"authorId\": \"1\", \"content\": \"hello\", \"tags\": []}");
        assertTrue(out.contains("🤖 speaking: Tie!"), "no answers → Tie!; got: " + out);
        assertEquals("🤖: good to hear @1", out.getLast(), "carousel must restart on re-entry");
    }

    @Test
    void play_again_restarts_questioning_without_reviving_the_stale_thanks_deadline() {
        List<String> out = run(START,
                "[login] {\"userId\": \"1\", \"isAdmin\": true}",
                "[new message] {\"authorId\": \"1\", \"content\": \"king\", \"tags\": [\"bot\"]}",
                "[1 hours elapsed]",
                "[5 seconds elapsed]",
                "[new message] {\"authorId\": \"1\", \"content\": \"play again\", \"tags\": [\"bot\"]}",
                "[3 minutes elapsed]",                            // must NOT exit king mode
                "[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": [\"bot\"]}");
        assertTrue(out.contains("🤖: KnowledgeKing is gonna start again!"), "got: " + out);
        assertEquals(2, out.stream().filter(Q0::equals).count(), "Q0 asked in both rounds; got: " + out);
        assertEquals("🤖: 1. 請問哪個 CSS 屬性可用於設置文字的顏色？\nA) text-align\nB) font-size\nC) color\nD) padding",
                out.getLast(), "the answer after play again must still be judged; got: " + out);
    }

    @Test
    void winner_announced_in_chat_when_someone_is_on_air() {
        List<String> out = run(START,
                "[login] {\"userId\": \"admin\", \"isAdmin\": true}",
                "[login] {\"userId\": \"1\", \"isAdmin\": false}",
                "[login] {\"userId\": \"2\", \"isAdmin\": false}",
                "[go broadcasting] {\"speakerId\": \"2\"}",
                "[new message] {\"authorId\": \"admin\", \"content\": \"king\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"C\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"1\", \"content\": \"A\", \"tags\": [\"bot\"]}");
        assertTrue(out.contains("🤖: The winner is 1"), "got: " + out);
        assertTrue(out.stream().noneMatch("🤖 go broadcasting..."::equals),
                "bot must not broadcast over a live speaker; got: " + out);
    }

    @Test
    void king_requires_admin_and_bot_tag_failures_are_silent() {
        List<String> out = run(START,
                "[login] {\"userId\": \"admin\", \"isAdmin\": true}",
                "[login] {\"userId\": \"5\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"5\", \"content\": \"king\", \"tags\": [\"bot\"]}",   // not admin
                "[new message] {\"authorId\": \"admin\", \"content\": \"king\", \"tags\": [\"5\"]}"); // bot not tagged
        assertTrue(out.stream().noneMatch(s -> s.contains("KnowledgeKing")), "got: " + out);
    }

    @Test
    void king_stop_is_admin_only_and_silent() {
        List<String> out = run(START,
                "[login] {\"userId\": \"1\", \"isAdmin\": true}",
                "[login] {\"userId\": \"2\", \"isAdmin\": false}",
                "[new message] {\"authorId\": \"1\", \"content\": \"king\", \"tags\": [\"bot\"]}",
                "[new message] {\"authorId\": \"2\", \"content\": \"king-stop\", \"tags\": [\"bot\"]}", // ignored
                "[new message] {\"authorId\": \"2\", \"content\": \"A\", \"tags\": [\"bot\"]}",          // still in king
                "[new message] {\"authorId\": \"1\", \"content\": \"king-stop\", \"tags\": [\"bot\"]}",  // exits king
                "[new message] {\"authorId\": \"2\", \"content\": \"hello\", \"tags\": []}");
        assertTrue(out.contains("🤖: Congrats! you got the answer! @2"),
                "non-admin king-stop must not end the game; got: " + out);
        assertEquals("🤖: good to hear @2", out.getLast(),
                "admin king-stop silently returns to normal; got: " + out);
    }
}
