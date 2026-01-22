package AdapterPattern.v2;

public interface VocabDictionary {
    Word lookup(String wordSpelling) throws WordNotExistException;
}
