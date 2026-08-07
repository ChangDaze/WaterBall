package app;

import domain.Community;
import domain.Member;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Composition root — ALL wiring happens here; objects never wire themselves.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            run(in.lines(), out::println);
        }
    }

    /** Full application wiring; the testcase harness runs this too. */
    public static void run(Stream<String> lines, Consumer<String> printer) {
        Community community = new Community(printer);
        Member botMember = community.registerBot("bot");
        WaterballBot bot = new WaterballBot(community, botMember);
        community.addListener(bot);
        bot.start();

        InputDispatcher dispatcher = new InputDispatcher(community);
        var iterator = lines.map(String::strip).filter(line -> !line.isEmpty()).iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.equals("[end]")) {
                break; // control signal for the harness, not a domain input
            }
            dispatcher.dispatch(line);
        }
    }
}
