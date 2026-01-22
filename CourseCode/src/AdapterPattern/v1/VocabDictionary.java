package AdapterPattern.v1;

public interface VocabDictionary {
    Word lookup(String wordSpelling) throws WordNotExistException;
}
