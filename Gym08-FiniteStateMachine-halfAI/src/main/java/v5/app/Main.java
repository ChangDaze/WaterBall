package v5.app;

import v5.domain.Community;
import v5.domain.Member;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        // global printer and reader
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))){
            run(in.lines(), out::println);
        }
    }

    /**
     * use static just for main call no need to new a Main to call run.
     */
    public static void run(Stream<String> lines, Consumer<String> printer){
        // main flow, create community and bot and register observer.
        Community community = new Community(printer);
        Member botMember = community.registerBot("bot");
        WaterballBot bot = new WaterballBot(community, botMember);
        community.addListener(bot);
        bot.start();// start bot fsm to decide initial state

        InputDispatcher dispatcher = new InputDispatcher(community);
        // wrap the stream with iterator and do chores before input to dispatcher
        Iterator<String> iterator = lines.map(String::strip).filter(line -> !line.isEmpty()).iterator();
        while (iterator.hasNext()){
            String line = iterator.next();
            // ending point
            if(line.equals("[end]")) {
                break;
            }
            dispatcher.dispatch(line);
        }
    }
}

