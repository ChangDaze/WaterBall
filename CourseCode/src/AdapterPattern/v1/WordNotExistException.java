package AdapterPattern.v1;

public class WordNotExistException extends RuntimeException{
    private final String wordSpelling;
    public WordNotExistException(String wordSpelling, Exception e){
        super(e);
        this.wordSpelling = wordSpelling;
    }

    public String getWordSpelling(){return wordSpelling;}
}
