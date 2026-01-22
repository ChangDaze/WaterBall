package AdapterPattern.v2;

import java.util.Scanner;

public class VocabLookupCLI {
    private static final Scanner in = new Scanner(System.in);
    private final VocabDictionary dictionary;

    public VocabLookupCLI(VocabDictionary dictionary){this.dictionary = dictionary;}

    public void start(){
        while (true){
            System.out.println("Lookup a word: ");
            String spelling = in.nextLine();
            if(!spelling.isBlank()) {
                try{
                    Word word = dictionary.lookup(spelling);
                    System.out.println(word);
                } catch (WordNotExistException e) {
                    System.out.printf("Can't find the word '%s'.\n", spelling);
                }
            }
        }
    }
}
