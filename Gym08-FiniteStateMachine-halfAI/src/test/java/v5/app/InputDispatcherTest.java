package v5.app;

import org.junit.jupiter.api.Test;
import v5.app.InputDispatcher;
import v5.domain.Community;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InputDispatcherTest {
    final List<String> out = new ArrayList<>();
    final Community community = new Community(out::add);
    final InputDispatcher dispatcher = new InputDispatcher(community);

    @Test
    void elapsed_regex_takes_precedence_and_supports_all_units() {
        dispatcher.dispatch("[started] {\"time\": \"2023-08-07 00:00:00\", \"quota\": 20}");
        dispatcher.dispatch("[10 seconds elapsed]");
        assertEquals(List.of("🕑 10 seconds elapsed..."), out);
        dispatcher.dispatch("[3 minutes elapsed]");
        dispatcher.dispatch("[2 hours elapsed]");
        assertEquals(LocalDateTime.of(2023, 8, 7, 2, 3, 10), community.clock().now());
    }

    @Test
    void general_commands_route_to_typed_community_calls() {
        dispatcher.dispatch("[started] {\"time\": \"2023-08-07 00:00:00\", \"quota\": 20}");
        dispatcher.dispatch("[login] {\"userId\": \"3\", \"isAdmin\": false}");
        dispatcher.dispatch("[new message] {\"authorId\": \"3\", \"content\": \"record\", \"tags\": [\"bot\"]}");
        assertEquals(List.of("💬 3: record @bot"), out);
        assertFalse(community.findOnlineMember("3").isAdmin());

        dispatcher.dispatch("[login] {\"userId\": \"9\", \"isAdmin\": true}");
        assertTrue(community.findOnlineMember("9").isAdmin());

        out.clear();
        dispatcher.dispatch("[go broadcasting] {\"speakerId\": \"3\"}");
        dispatcher.dispatch("[speak] {\"speakerId\": \"3\", \"content\": \"Test\"}");
        dispatcher.dispatch("[stop broadcasting] {\"speakerId\": \"3\"}");
        assertEquals(List.of("📢 3 is broadcasting...", "📢 3: Test", "📢 3 stop broadcasting"), out);

        out.clear();
        dispatcher.dispatch("[new post] {\"id\": \"1\", \"authorId\": \"3\", \"title\": \"T\", \"content\": \"C\", \"tags\": [\"9\"]}");
        assertEquals(List.of("3: 【T】C @9"), out);
    }

    @Test
    void unknown_command_and_bad_json_throw_loudly() {
        assertThrows(IllegalArgumentException.class, () -> dispatcher.dispatch("[frobnicate]"));
        assertThrows(IllegalArgumentException.class, () -> dispatcher.dispatch("not a command at all"));
        assertThrows(RuntimeException.class, () -> dispatcher.dispatch("[login] {broken json"));
    }
}
