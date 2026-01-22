package AdapterPattern.v2;

import AdapterPattern.v2.*;
import AdapterPattern.v2.crawler.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VocabCrawlerAdapter implements VocabDictionary {
    private final SuperVocabCrawler superVocabCrawler = new SuperVocabCrawler();

    @Override
    public Word lookup(String wordSpelling) throws WordNotExistException{
        try{
            SuperWORD word = superVocabCrawler.crawl(wordSpelling);
            return converToWord(word);
        } catch (YouSpellItWrongException e) {
            throw new WordNotExistException(wordSpelling, e);
        }
    }

    private Word converToWord(SuperWORD word){
        String spelling = word.raw;
        Map<Word.PartOfSpeech, List<String>> definitions = new LinkedHashMap<>();

        for (String definitionLine: word.definitions){
            String[] splits = definitionLine.split("\\s+", 2);
            Word.PartOfSpeech partOfSpeech = covertPartOfSpeech(splits[0]);
            String definition = splits[1];
            definitions.computeIfAbsent(partOfSpeech, k -> new ArrayList<>())
                    .add(definition);
        }
        return new Word(spelling, definitions);
    }

    private Word.PartOfSpeech covertPartOfSpeech(String partOfSpeech){
        switch (partOfSpeech){
            case "noun":
                return Word.PartOfSpeech.NOUN;
            case "verb":
                return Word.PartOfSpeech.VERB;
            case "adverb":
                return Word.PartOfSpeech.ADV;
            case "adjective":
                return Word.PartOfSpeech.ADJ;
            case "article":
                return Word.PartOfSpeech.ARTICLE;
            case "preposition":
                return Word.PartOfSpeech.PREPOSITION;
            case "conjunction":
                return Word.PartOfSpeech.CONJUNCTION;
            case "interjection":
                return Word.PartOfSpeech.INTERJECTION;
            //好像沒介係詞
            default:
                throw new IllegalArgumentException(String.format("Unsupported partOfSpeech '%s'.", partOfSpeech));
        }
    }
}
