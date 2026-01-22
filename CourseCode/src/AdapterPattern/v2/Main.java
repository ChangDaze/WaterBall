package AdapterPattern.v2;

public class Main {
    public static void main(String[] args) {
        VocabLookupCLI cli = new VocabLookupCLI(new VocabCrawlerAdapter());
        cli.start();
    }
}
